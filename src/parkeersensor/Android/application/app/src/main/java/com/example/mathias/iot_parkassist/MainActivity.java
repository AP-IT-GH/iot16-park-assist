/////////////////////////////////////////////////////////////////////
//Please don't look at this code, since it is all hardcoded.       //
//May the Code Gods forgive me for this blasphemy that I call code.//
/////////////////////////////////////////////////////////////////////


package com.example.mathias.iot_parkassist;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private Canvas canvas;
    final Paint myPaint = new Paint();
    private int width, height;
    private int top, left, bottom, right;
    private int sensorWidth, sensorHeight;
    private int touchMargin;
    private boolean topBool, leftBool, bottomBool, rightBool = false;
    private boolean toggleAdd, toggleDelete = false;
    private ArrayList<Integer> xCoöList, yCoöList = new ArrayList<Integer>();
    private ArrayList<String> macList = new ArrayList<String>();
    ImageView drawingSpace;
    SharedPreferences sharedPreferencesX;
    SharedPreferences sharedPreferencesY;
    SharedPreferences.Editor editor;
    private static final String LIST_X = "listXCoördinates";
    private static final String LIST_Y = "listYCoördinates";
    bluetooth bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new bluetooth(this);
        new mqttClass(this);
        bt.getPairedDevices();
        new LooperTask().execute(bt.pairedDevices.iterator().next());


        drawingSpace = (ImageView) findViewById(R.id.drawingSpace);
        //final View content = findViewById(android.R.id.content);
        drawingSpace.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Remove it here unless you want to get this callback for EVERY
                //layout pass, which can get you into infinite loops if you ever
                //modify the layout from within this method.
                drawingSpace.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                drawCenter();


                drawAllSensors(0);

            }
        });

        drawingSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    //case MotionEvent.ACTION_DOWN:
                    //case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        if(toggleAdd) {

                            addSensor(x, y, 0, "20:16:01:26:15:73");
                            toggleAdd = false;
                        } else if(toggleDelete) {
                            deleteSensor(x,y);
                        }
                        break;
                }

                return true;
            }
        });

        Button buttonAdd = (Button) findViewById(R.id.add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleAdd = true;
            }
        });

        Button buttonDelete = (Button) findViewById(R.id.delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleDelete = true;
            }
        });

        Button buttonStart = (Button) findViewById(R.id.start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("Y");
            }
        });
        Button buttonStop = (Button) findViewById(R.id.stop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("N");
            }
        });
    }

    private class LooperTask extends AsyncTask<BluetoothDevice, String, String> {

        @Override
        protected String doInBackground(BluetoothDevice... device) {
            BluetoothConnector bc = new BluetoothConnector(device[0], true, bt.bluetoothAdapter, null);
            try {
                BluetoothConnector.BluetoothSocketWrapper bs = bc.connect();
                InputStream input = bs.getInputStream();
                while (true) {
                    String distanceString = bt.read(device[0], input);
                    Log.e("distanceAs", distanceString);
                    //Long distance;
                    try {
                        //distance = Long.getLong(distanceString);
                        publishProgress(distanceString, device[0].toString());
                    } catch (NumberFormatException nfe) {
                        Log.e("nfe", nfe.toString());
                    }

                    SystemClock.sleep(300);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

       // @Override
        protected void onProgressUpdate(String... data) {
            int distance = 0;
            try {
                distance = new Integer(data[0].trim());
                Log.e("progress", String.valueOf(distance));
            } catch (NumberFormatException nfe) {
                Log.e("nfe", nfe.toString());
            }

            sharedPreferencesX = getSharedPreferences(LIST_X, Context.MODE_PRIVATE);
            sharedPreferencesY = getSharedPreferences(LIST_Y, Context.MODE_PRIVATE);

            String mac = data[1].trim();
            Log.e("mac", mac);
            //bitmap.eraseColor(Color.TRANSPARENT);
            drawCenter();
            addSensor(sharedPreferencesX.getInt(mac, -1), sharedPreferencesY.getInt(mac,-1), distance, mac);

            //drawAllSensors(distance);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void drawCenter() {
        width = drawingSpace.getWidth();
        height = drawingSpace.getHeight();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        drawingSpace.setImageBitmap(bitmap);

        top = left = 300;
        right = width-300;
        bottom = height-300;
        sensorWidth = 40;
        sensorHeight = sensorWidth*2;
        touchMargin = sensorWidth;

        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);

        canvas.drawRect(left, top, right, bottom, myPaint);

    }

    private void drawAllSensors(int distance) {
        bt.getPairedDevices();
        if (!bt.pairedDevices.isEmpty()) {
            //new ConnectThread(pairedDevices.iterator().next()).run();
            /*for (int j = 0; j<=bt.pairedDevices.size(); j++) {
                Log.e("pairedDevice:    ", bt.pairedDevices.iterator().next().toString());
                macList.add(bt.pairedDevices.iterator().next().toString());
            }*/
            Iterator iter = bt.pairedDevices.iterator();
            while(iter.hasNext()){
                //Log.e("iter:    ", iter.next().toString());
                macList.add(iter.next().toString());
            }
        }

        sharedPreferencesX = getSharedPreferences(LIST_X, Context.MODE_PRIVATE);
        sharedPreferencesY = getSharedPreferences(LIST_Y, Context.MODE_PRIVATE);

        xCoöList = getCoöList(sharedPreferencesX);
        yCoöList = getCoöList(sharedPreferencesY);

        clearSharedPreferences(sharedPreferencesX);
        clearSharedPreferences(sharedPreferencesY);

        fillSharedPreferences(sharedPreferencesX, xCoöList);
        fillSharedPreferences(sharedPreferencesY, yCoöList);

        myPaint.setColor(Color.rgb(200, 200, 100));

        for (int i=0; i<xCoöList.size(); i++) {
            addSensor(xCoöList.get(i),yCoöList.get(i), distance, macList.get(i));
            //drawDistance(xCoöList.get(i), yCoöList.get(i), 80);
        }
    }

    private void addSensor(int x, int y, int distance, String mac) {


        //necessary to have the sensor drawn with the touch point as middle. Momenteel niet in gebruik!!!!
        int ySide = y;
        int xSide = x;

        if (x >= left-touchMargin && x <= left+touchMargin){
            x = left-sensorWidth;
            leftBool = true;
            //ySide = y-sensorWidth;
        } else if (x >= right-touchMargin && x <= right+touchMargin) {
            x = right;
            rightBool = true;
            //ySide = y-sensorWidth;
        }
        if (y >= top-touchMargin && y <= top+touchMargin) {
            y = top-sensorWidth;
            topBool = true;
            //xSide = x-sensorWidth;
        } else if (y >= bottom-touchMargin && y <= bottom+touchMargin) {
            y = bottom;
            bottomBool = true;
            //xSide = x-sensorWidth;
        }

        if (leftBool && topBool) {
            drawSensor(x,y, sensorHeight, sensorWidth);
            drawSensor(x,y, sensorWidth, sensorHeight);
            drawDistance(x,y, distance);
        } else if (leftBool && bottomBool) {
            drawSensor(x,y, sensorHeight, sensorWidth);
            drawSensor(x,y-sensorWidth, sensorWidth, sensorHeight );
            drawDistance(x,y, distance);
        } else if (rightBool && topBool) {
            drawSensor(x,y, sensorWidth, sensorHeight);
            drawSensor(x-sensorWidth,y, sensorHeight, sensorWidth);
            drawDistance(x,y, distance);
        } else if (rightBool && bottomBool) {
            drawSensor(x-sensorWidth,y, sensorHeight, sensorWidth);
            drawSensor(x,y-sensorWidth, sensorWidth, sensorHeight);
            drawDistance(x,y, distance);
        } else if (leftBool || rightBool) {
            if (y > top && y < bottom) {
                drawSensor(x, ySide, sensorWidth, sensorHeight);
                drawDistance(x,y, distance);
                //y = ySide;
            }
        } else if (topBool || bottomBool) {
            if (x > left && x < right) {
                drawSensor(xSide, y, sensorHeight, sensorWidth);
                drawDistance(x,y, distance);
                //x = xSide;
            }
        }

        topBool = rightBool = bottomBool = leftBool = false;

        //z = mac address of the sensor that has been selected to add.
        //TODO: check of de coo al in de arrays zitten, want dan moeten deze niet weer toegevoegd worden.
        editor = sharedPreferencesX.edit().putInt(mac, x);
        editor.commit();
        editor = sharedPreferencesY.edit().putInt(mac, y);
        editor.commit();

    }

    private void drawSensor(int x, int y, int sensorDrawWidth, int sensorDrawHeight) {
        myPaint.setColor(Color.rgb(200, 200, 100));
        Rect sensor = new Rect(x, y, x+sensorDrawWidth, y+sensorDrawHeight);
        canvas.drawRect(sensor, myPaint);

        drawingSpace.setImageBitmap(bitmap);


    }

    private ArrayList<Integer> getCoöList(SharedPreferences sharedPreferences) {
        ArrayList<Integer> coöList = new ArrayList<Integer>();
        for (int i = 0; i < macList.size(); i++) {
            coöList.add(sharedPreferences.getInt(macList.get(i),-1));
        }
        return coöList;
    }

    private void clearSharedPreferences(SharedPreferences sharedPreferences) {
        editor = sharedPreferences.edit().clear();
        editor.commit();
    }

    private void fillSharedPreferences(SharedPreferences sharedPreferences, ArrayList<Integer> coöList) {
        for (int i=0; i<coöList.size(); i++) {
            editor = sharedPreferences.edit();
            editor.putInt(macList.get(i), coöList.get(i));
        }
    }

    private void deleteSensor(int x, int y) {
        /*//!!!!!!!!Voor verwijderen bluetooth nodig, denk dat het op deze manier werkt maar niet zeker (linkerbovenhoek).

        //Log.e("in delete", String.valueOf(toggleDelete));
        xCoöList = yCoöList = new ArrayList<Integer>();

        xCoöList = getCoöList(sharedPreferencesX);
        yCoöList = getCoöList(sharedPreferencesY);


        myPaint.setColor(Color.rgb(255, 255, 255));
        for (int i=0; i<xCoöList.size(); i++) {
            addSensor(xCoöList.get(i),yCoöList.get(i), 200);
        }
        Log.e("size", String.valueOf(xCoöList.size()));
        for (int i=0; i<xCoöList.size(); i++) {
            int xList = xCoöList.get(i);
            int yList = yCoöList.get(i);

            if (x >= left-touchMargin && x <= left+touchMargin){

                leftBool = true;
                //Log.e("Left bool", String.valueOf(leftBool));

            } else if (x >= right-touchMargin && x <= right+touchMargin) {

                rightBool = true;

            }
            if (y >= top-touchMargin && y <= top+touchMargin) {

                topBool = true;
                //Log.e("top bool", String.valueOf(topBool));
            } else if (y >= bottom-touchMargin && y <= bottom+touchMargin) {

                bottomBool = true;

            }

            if (leftBool && topBool) {
                Log.e("x", String.valueOf(x));
                Log.e("xList", String.valueOf(xList));
                Log.e("y", String.valueOf(y));
                Log.e("yList", String.valueOf(yList));
                if (x >= xList && x <= xList+sensorHeight && y >= yList && y <= yList+sensorHeight) {
                    Log.e("in linkerbovenhoek", "ja hoor");
                    xCoöList.remove(i);
                    yCoöList.remove(i);
                    toggleDelete = false;
                }
            } else if (leftBool && bottomBool) {
                                    /*if (x >= xList && x <= xList+sensorHeight && y >= yList-sensorWidth && y <= yList) {

                                    }*//*
            } else if (rightBool && topBool) {

            } else if (rightBool && bottomBool) {

            } else if (leftBool || rightBool) {
                if (y > top && y < bottom) {

                }
            } else if (topBool || bottomBool) {
                if (x > left && x < right) {

                }
            }

            topBool = rightBool = bottomBool = leftBool = false;


        }
        clearSharedPreferences(sharedPreferencesX);
        clearSharedPreferences(sharedPreferencesY);

        fillSharedPreferences(sharedPreferencesX, xCoöList);
        fillSharedPreferences(sharedPreferencesY, yCoöList);


        myPaint.setColor(Color.rgb(200, 200, 100));

        for (int j=0; j<xCoöList.size(); j++) {
            addSensor(xCoöList.get(j),yCoöList.get(j), 200);
        }
*/
    }

    private void drawDistance(int x, int y, int distance) {
        if (x >= left - touchMargin && x <= left + touchMargin) {

            leftBool = true;
            //Log.e("Left bool", String.valueOf(leftBool));

        } else if (x >= right - touchMargin && x <= right + touchMargin) {

            rightBool = true;

        }
        if (y >= top - touchMargin && y <= top + touchMargin) {

            topBool = true;
            //Log.e("top bool", String.valueOf(topBool));
        } else if (y >= bottom - touchMargin && y <= bottom + touchMargin) {

            bottomBool = true;

        }

        Path path = new Path();
        //path.setFillType(Path.FillType.EVEN_ODD);
        float nextPointC = 50;
        float sideTriangle = (float) 32.25; // for sides
        float nextPointS = (float) 32.25;
        float divider = (float) 1.550387596899225;
        if (leftBool && topBool) {

            /*path.moveTo(x, y);
            path.lineTo(x - 200, y);
            path.lineTo(x, y - 200);
            path.lineTo(x, y);*/
            if (distance >= 0) {

                if (distance < 50) {
                    nextPointC = distance;
                }
                //red triangle
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 0, 0));
                path.moveTo(x, y);
                path.lineTo(x - nextPointC, y);
                path.lineTo(x, y - nextPointC);
                path.lineTo(x, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 50) {
                if (distance < 100) {
                    nextPointC = distance - 50;
                }
                //orange part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 97, 0));
                path.moveTo(x - 50, y);
                path.lineTo(x - 50 - nextPointC, y);
                path.lineTo(x, y - 50 - nextPointC);
                path.lineTo(x, y - 50);
                path.lineTo(x - 50, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 100) {
                if (distance < 150) {
                    nextPointC = distance - 100;
                    Log.e("distance: ", String.valueOf(distance));
                }
                //yellow part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 255, 0));
                path.moveTo(x - 100, y);
                path.lineTo(x - 100 - nextPointC, y);
                path.lineTo(x, y - 100 - nextPointC);
                path.lineTo(x, y - 100);
                path.lineTo(x - 100, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 150) {
                if (distance < 200) {
                    nextPointC = distance - 150;
                }
                //green part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(0, 255, 0));
                path.moveTo(x - 150, y);
                path.lineTo(x - 150 - nextPointC, y);
                path.lineTo(x, y - 150 - nextPointC);
                path.lineTo(x, y - 150);
                path.lineTo(x - 150, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }

        } else if (leftBool && bottomBool) {
            /*path.moveTo(x, y + sensorWidth);
            path.lineTo(x, y + 200 + sensorWidth);
            path.lineTo(x - 200, y + sensorWidth);
            path.lineTo(x, y + sensorWidth);*/
            if (distance >= 0) {

                if (distance < 50) {
                    nextPointC = distance;
                }
                //red triangle
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 0, 0));
                path.moveTo(x, y + sensorWidth);
                path.lineTo(x - nextPointC, y + sensorWidth);
                path.lineTo(x, y + nextPointC + sensorWidth);
                path.lineTo(x, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 50) {

                if (distance < 100) {
                    nextPointC = distance - 50;
                }
                //orange part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 97, 0));
                path.moveTo(x - 50, y + sensorWidth);
                path.lineTo(x - 50 - nextPointC, y + sensorWidth);
                path.lineTo(x, y + 50 + nextPointC + sensorWidth);
                path.lineTo(x, y + 50 + sensorWidth);
                path.lineTo(x - 50, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 100) {

                if (distance < 150) {
                    nextPointC = distance - 100;
                }
                //yellow part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 255, 0));
                path.moveTo(x - 100, y + sensorWidth);
                path.lineTo(x - 100 - nextPointC, y + sensorWidth);
                path.lineTo(x, y + 100 + nextPointC + sensorWidth);
                path.lineTo(x, y + 100 + sensorWidth);
                path.lineTo(x - 100, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 150) {

                if (distance < 200) {
                    nextPointC = distance - 150;
                }
                //green part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(0, 255, 0));
                path.moveTo(x - 150, y + sensorWidth);
                path.lineTo(x - 150 - nextPointC, y + sensorWidth);
                path.lineTo(x, y + 150 + nextPointC + sensorWidth);
                path.lineTo(x, y + 150 + sensorWidth);
                path.lineTo(x - 150, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }

        } else if (rightBool && topBool) {
            /*path.moveTo(x + sensorWidth, y);
            path.lineTo(x + 200, y);
            path.lineTo(x + sensorWidth, y - 200);
            path.lineTo(x + sensorWidth, y);*/
            if (distance >= 0) {

                if (distance < 50) {
                    nextPointC = distance;
                }
                //red triangle
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 0, 0));
                path.moveTo(x + sensorWidth, y);
                path.lineTo(x + nextPointC + sensorWidth, y);
                path.lineTo(x + sensorWidth, y - nextPointC);
                path.lineTo(x + sensorWidth, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 50) {

                if (distance < 100) {
                    nextPointC = distance - 50;
                }
                //orange part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 97, 0));
                path.moveTo(x + 50 + sensorWidth, y);
                path.lineTo(x + 50 + nextPointC + sensorWidth, y);
                path.lineTo(x + sensorWidth, y - 50 - nextPointC);
                path.lineTo(x + sensorWidth, y - 50);
                path.lineTo(x + 50 + sensorWidth, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 100) {

                if (distance < 150) {
                    nextPointC = distance - 100;
                }
                //yellow part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 255, 0));
                path.moveTo(x + 100 + sensorWidth, y);
                path.lineTo(x + 100 + nextPointC + sensorWidth, y);
                path.lineTo(x + sensorWidth, y - 100 - nextPointC);
                path.lineTo(x + sensorWidth, y - 100);
                path.lineTo(x + 100 + sensorWidth, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 150) {

                if (distance < 200) {
                    nextPointC = distance - 150;
                }
                //green part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(0, 255, 0));
                path.moveTo(x + 150 + sensorWidth, y);
                path.lineTo(x + 150 + nextPointC + sensorWidth, y);
                path.lineTo(x + sensorWidth, y - 150 - nextPointC);
                path.lineTo(x + sensorWidth, y - 150);
                path.lineTo(x + 150 + sensorWidth, y);

                path.close();
                canvas.drawPath(path, myPaint);
            }
        } else if (rightBool && bottomBool) {
            /*path.moveTo(x + sensorWidth, y + sensorWidth);
            path.lineTo(x + sensorWidth, y + 200 + sensorWidth);
            path.lineTo(x + 200 + sensorWidth, y + sensorWidth);
            path.lineTo(x + sensorWidth, y + sensorWidth);*/
            if (distance >= 0) {

                if (distance < 50) {
                    nextPointC = distance;
                }
                //red triangle
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 0, 0));
                path.moveTo(x + sensorWidth, y + sensorWidth);
                path.lineTo(x + nextPointC + sensorWidth, y + sensorWidth);
                path.lineTo(x + sensorWidth, y + nextPointC + sensorWidth);
                path.lineTo(x + sensorWidth, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 50) {

                if (distance < 100) {
                    nextPointC = distance - 50;
                }
                //orange part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 97, 0));
                path.moveTo(x + 50 + sensorWidth, y + sensorWidth);
                path.lineTo(x + 50 + nextPointC + sensorWidth, y + sensorWidth);
                path.lineTo(x + sensorWidth, y + 50 + nextPointC + sensorWidth);
                path.lineTo(x + sensorWidth, y + 50 + sensorWidth);
                path.lineTo(x + 50 + sensorWidth, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 100) {

                if (distance < 150) {
                    nextPointC = distance - 100;
                }
                //yellow part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(255, 255, 0));
                path.moveTo(x + 100 + sensorWidth, y + sensorWidth);
                path.lineTo(x + 100 + nextPointC + sensorWidth, y + sensorWidth);
                path.lineTo(x + sensorWidth, y + 100 + nextPointC + sensorWidth);
                path.lineTo(x + sensorWidth, y + 100 + sensorWidth);
                path.lineTo(x + 100 + sensorWidth, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }
            if (distance >= 150) {

                if (distance < 200) {
                    nextPointC = distance - 150;
                }
                //green part
                path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                myPaint.setColor(Color.rgb(0, 255, 0));
                path.moveTo(x + 150 + sensorWidth, y + sensorWidth);
                path.lineTo(x + 150 + nextPointC + sensorWidth, y + sensorWidth);
                path.lineTo(x + sensorWidth, y + 150 + nextPointC + sensorWidth);
                path.lineTo(x + sensorWidth, y + 150 + sensorWidth);
                path.lineTo(x + 150 + sensorWidth, y + sensorWidth);

                path.close();
                canvas.drawPath(path, myPaint);
            }
        } else if (leftBool) {
            if (y > top && y < bottom) {
                /*path.moveTo(x, y+sensorWidth);
                path.lineTo(x - 141, y+sensorWidth-141);
                path.lineTo(x-141, y +sensorWidth + 141);
                path.lineTo(x, y+sensorWidth);*/


                if (distance >= 0) {

                    if (distance < 50) {
                        nextPointS = (distance/divider);
                    }
                    //red triangle
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 0, 0));
                    path.moveTo(x, y + sensorWidth);
                    path.lineTo(x - nextPointS, y + sensorWidth - nextPointS);
                    path.lineTo(x - nextPointS, y + nextPointS + sensorWidth);
                    path.lineTo(x, y + sensorWidth);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 50) {

                    if (distance < 100) {
                        nextPointS = ((distance - 50) / divider);
                    }
                    //orange part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 97, 0));
                    path.moveTo(x - sideTriangle, y + sensorWidth - sideTriangle);
                    path.lineTo(x - nextPointS - sideTriangle, y + sensorWidth - nextPointS - sideTriangle);
                    path.lineTo(x - nextPointS - sideTriangle, y + sensorWidth + nextPointS + sideTriangle);
                    path.lineTo(x - sideTriangle, y + sensorWidth + sideTriangle);
                    path.lineTo(x - sideTriangle, y + sensorWidth - sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 100) {

                    if (distance < 150) {
                        nextPointS = ((distance - 100) / divider);
                    }
                    //yellow part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 255, 0));
                    path.moveTo(x - 2 * sideTriangle, y + sensorWidth - 2 * sideTriangle);
                    path.lineTo(x - nextPointS - 2 * sideTriangle, y + sensorWidth - nextPointS - 2 * sideTriangle);
                    path.lineTo(x - nextPointS - 2 * sideTriangle, y + sensorWidth + nextPointS + 2 * sideTriangle);
                    path.lineTo(x - 2 * sideTriangle, y + sensorWidth + 2 * sideTriangle);
                    path.lineTo(x - 2 * sideTriangle, y + sensorWidth - 2 * sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 150) {

                    if (distance < 200) {
                        nextPointS = ((distance - 150) / divider);
                    }
                    //green part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(0, 255, 0));
                    path.moveTo(x - 3 * sideTriangle, y + sensorWidth - 3 * sideTriangle);
                    path.lineTo(x - 3 * sideTriangle, y + sensorWidth + 3 * sideTriangle);
                    path.lineTo(x - nextPointS - 3 * sideTriangle, y + sensorWidth + nextPointS + 3 * sideTriangle);
                    path.lineTo(x - nextPointS - 3 * sideTriangle, y + sensorWidth - nextPointS - 3 * sideTriangle);
                    path.lineTo(x - 3 * sideTriangle, y + sensorWidth - 3 * sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }

            }
        } else if (rightBool) {
            if (y > top && y < bottom) {
                /*path.moveTo(x+sensorWidth, y+sensorWidth);
                path.lineTo(x +sensorWidth+ 141, y+sensorWidth-141);
                path.lineTo(x+sensorWidth+141, y +sensorWidth + 141);
                path.lineTo(x+sensorWidth, y+sensorWidth);*/


                if (distance >= 0) {

                    if (distance < 50) {
                        nextPointS = (distance / divider);
                    }
                    //red triangle
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 0, 0));
                    path.moveTo(x + sensorWidth, y + sensorWidth);
                    path.lineTo(x + sensorWidth + nextPointS, y + sensorWidth - nextPointS);
                    path.lineTo(x + sensorWidth + nextPointS, y + nextPointS + sensorWidth);
                    path.lineTo(x + sensorWidth, y + sensorWidth);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 50) {

                    if (distance < 100) {
                        nextPointS = ((distance - 50) / divider);
                    }
                    //orange part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 97, 0));
                    path.moveTo(x + sensorWidth + sideTriangle, y + sensorWidth - sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + sideTriangle, y + sensorWidth - nextPointS - sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + sideTriangle, y + sensorWidth + nextPointS + sideTriangle);
                    path.lineTo(x + sensorWidth + sideTriangle, y + sensorWidth + sideTriangle);
                    path.lineTo(x + sensorWidth + sideTriangle, y + sensorWidth - sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 100) {

                    if (distance < 150) {
                        nextPointS = ((distance - 100) / divider);
                    }
                    //yellow part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 255, 0));
                    path.moveTo(x + sensorWidth + 2 * sideTriangle, y + sensorWidth - 2 * sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + 2* sideTriangle, y + sensorWidth - nextPointS - 2 * sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + 2 * sideTriangle, y + sensorWidth + nextPointS + 2 * sideTriangle);
                    path.lineTo(x + sensorWidth + 2 * sideTriangle, y + sensorWidth + 2 * sideTriangle);
                    path.lineTo(x + sensorWidth + 2 * sideTriangle, y + sensorWidth - 2 * sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 150) {

                    if (distance < 200) {
                        nextPointS = ((distance - 150) / divider);
                    }
                    //green part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(0, 255, 0));
                    path.moveTo(x + sensorWidth + 3 * sideTriangle, y + sensorWidth - 3 * sideTriangle);
                    path.lineTo(x + sensorWidth + 3 * sideTriangle, y + sensorWidth + 3 * sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + 3 * sideTriangle, y + sensorWidth + nextPointS + 3 * sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + 3 * sideTriangle, y + sensorWidth - nextPointS - 3 * sideTriangle);
                    path.lineTo(x + sensorWidth + 3 * sideTriangle, y + sensorWidth - 3 * sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
            }
        }else if (topBool) {
            if (x > left && x < right) {
                /*path.moveTo(x+sensorWidth, y);
                path.lineTo(x +sensorWidth+ 141, y-141);
                path.lineTo(x+sensorWidth-141, y - 141);
                path.lineTo(x+sensorWidth, y);*/


                if (distance >= 0) {

                    if (distance < 50) {
                        nextPointS = (distance / divider);
                    }
                    //red triangle
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 0, 0));
                    path.moveTo(x + sensorWidth, y);
                    path.lineTo(x + sensorWidth + nextPointS, y - nextPointS);
                    path.lineTo(x + sensorWidth - nextPointS, y - nextPointS);
                    path.lineTo(x + sensorWidth, y);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 50) {

                    if (distance < 100) {
                        nextPointS = ((distance - 50) / divider);
                    }
                    //orange part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 97, 0));
                    path.moveTo(x + sensorWidth + sideTriangle, y - sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + sideTriangle, y - nextPointS - sideTriangle);
                    path.lineTo(x + sensorWidth - nextPointS - sideTriangle, y - nextPointS - sideTriangle);
                    path.lineTo(x + sensorWidth - sideTriangle, y - sideTriangle);
                    path.lineTo(x + sensorWidth + sideTriangle, y - sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 100) {

                    if (distance < 150) {
                        nextPointS = ((distance - 100) / divider);
                    }
                    //yellow part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 255, 0));
                    path.moveTo(x + sensorWidth + 2 * sideTriangle, y - 2 * sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + 2 * sideTriangle, y - nextPointS - 2 * sideTriangle);
                    path.lineTo(x + sensorWidth - nextPointS - 2 * sideTriangle, y - nextPointS - 2 * sideTriangle);
                    path.lineTo(x + sensorWidth - 2 * sideTriangle, y - 2 * sideTriangle);
                    path.lineTo(x + sensorWidth + 2 * sideTriangle, y - 2 * sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 150) {

                    if (distance < 200) {
                        nextPointS = ((distance - 150) / divider);
                    }
                    //green part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(0, 255, 0));
                    path.moveTo(x + sensorWidth + 3 * sideTriangle, y - 3 * sideTriangle);
                    path.lineTo(x + sensorWidth - 3 * sideTriangle, y - 3 * sideTriangle);
                    path.lineTo(x + sensorWidth - nextPointS-3 * sideTriangle, y - nextPointS - 3 * sideTriangle);
                    path.lineTo(x + sensorWidth + nextPointS + 3 * sideTriangle, y - nextPointS - 3 * sideTriangle);
                    path.lineTo(x + sensorWidth + 3 * sideTriangle, y - 3 * sideTriangle);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
            }
        }else if (bottomBool) {
            if (x > left && x < right) {
                /*path.moveTo(x+sensorWidth, y+sensorWidth);
                path.lineTo(x +sensorWidth+ 141, y+sensorWidth+141);
                path.lineTo(x+sensorWidth-141, y+sensorWidth + 141);
                path.lineTo(x+sensorWidth, y+sensorWidth);*/


                if (distance >= 0) {

                    if (distance < 50) {
                        nextPointS = (distance / divider);
                    }
                    //red triangle
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 0, 0));
                    path.moveTo(x + sensorWidth, y + sensorWidth);
                    path.lineTo(x + sensorWidth + nextPointS, y + nextPointS + sensorWidth);
                    path.lineTo(x + sensorWidth - nextPointS, y + nextPointS + sensorWidth);
                    path.lineTo(x + sensorWidth, y + sensorWidth);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 50) {

                    if (distance < 100) {
                        nextPointS = ((distance - 50) / divider);
                    }
                    //orange part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 97, 0));
                    path.moveTo(x + sensorWidth + sideTriangle, y + sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth + nextPointS + sideTriangle, y + nextPointS + sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth - nextPointS - sideTriangle, y + nextPointS + sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth - sideTriangle, y + sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth + sideTriangle, y + sideTriangle + sensorWidth);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 100) {

                    if (distance < 150) {
                        nextPointS = ((distance - 100) / divider);
                    }
                    //yellow part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(255, 255, 0));
                    path.moveTo(x + sensorWidth + 2 * sideTriangle, y + 2 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth + nextPointS + 2 * sideTriangle, y +  nextPointS + 2 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth - nextPointS - 2 * sideTriangle, y + nextPointS + 2 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth - 2 * sideTriangle, y + 2 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth + 2 * sideTriangle, y + 2 * sideTriangle + sensorWidth);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
                if (distance >= 150) {

                    if (distance < 200) {
                        nextPointS = ((distance - 150) / divider);
                    }
                    //green part
                    path = new Path();
                    path.setFillType(Path.FillType.EVEN_ODD);
                    myPaint.setColor(Color.rgb(0, 255, 0));
                    path.moveTo(x + sensorWidth + 3 * sideTriangle, y + 3 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth - 3 * sideTriangle, y + 3 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth - nextPointS - 3 * sideTriangle, y + nextPointS + 3 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth + nextPointS + 3 * sideTriangle, y + nextPointS + 3 * sideTriangle + sensorWidth);
                    path.lineTo(x + sensorWidth + 3 * sideTriangle, y + 3 * sideTriangle + sensorWidth);

                    path.close();
                    canvas.drawPath(path, myPaint);
                }
            }
        }


        topBool = rightBool = bottomBool = leftBool = false;
        /*path.close();
        //myPaint.setColor(android.graphics.Color.RED);
        //myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //myPaint.setAntiAlias(true);

        canvas.drawPath(path, myPaint);*/
        drawingSpace.setImageBitmap(bitmap);
    }

    private void drawSideTriangle(int x,int y, int xSensorWidth, int ySensorWidth) {
        float sideTriangle = (float)32.25;

        //red triangle
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        myPaint.setColor(Color.rgb(255, 0, 0));
        path.moveTo(x+xSensorWidth,y+ySensorWidth);
        path.lineTo(x+xSensorWidth+sideTriangle, y+sideTriangle+ySensorWidth);
        path.lineTo(x+xSensorWidth-sideTriangle, y+sideTriangle+ySensorWidth);
        path.lineTo(x+xSensorWidth,y+ySensorWidth);

        path.close();
        canvas.drawPath(path, myPaint);

        //orange part
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        myPaint.setColor(Color.rgb(255, 97, 0));
        path.moveTo(x+sensorWidth+sideTriangle,y+sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth+2*sideTriangle, y+2*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth-2*sideTriangle, y+2*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth-sideTriangle, y+sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth+sideTriangle,y+sideTriangle+sensorWidth);

        path.close();
        canvas.drawPath(path, myPaint);

        //yellow part
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        myPaint.setColor(Color.rgb(255, 255, 0));
        path.moveTo(x+sensorWidth+2*sideTriangle,y+2*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth+3*sideTriangle, y+3*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth-3*sideTriangle, y+3*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth-2*sideTriangle, y+2*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth+2*sideTriangle,y+2*sideTriangle+sensorWidth);

        path.close();
        canvas.drawPath(path, myPaint);

        //green part
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        myPaint.setColor(Color.rgb(0, 255, 0));
        path.moveTo(x+sensorWidth+3*sideTriangle,y+3*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth-3*sideTriangle, y+3*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth-4*sideTriangle, y+4*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth+4*sideTriangle, y+4*sideTriangle+sensorWidth);
        path.lineTo(x+sensorWidth+3*sideTriangle,y+3*sideTriangle+sensorWidth);

        path.close();
        canvas.drawPath(path, myPaint);
    }

}

