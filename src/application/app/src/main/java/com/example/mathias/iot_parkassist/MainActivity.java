package com.example.mathias.iot_parkassist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingSpace = (ImageView) findViewById(R.id.drawingSpace);
        //final View content = findViewById(android.R.id.content);
        drawingSpace.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Remove it here unless you want to get this callback for EVERY
                //layout pass, which can get you into infinite loops if you ever
                //modify the layout from within this method.
                drawingSpace.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                //Now you can get the width and height from content
                width = drawingSpace.getWidth();
                height = drawingSpace.getHeight();
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                drawingSpace.setImageBitmap(bitmap);

                myPaint.setColor(Color.rgb(0, 0, 0));
                myPaint.setStrokeWidth(10);

                top = left = 300;
                right = width-300;
                bottom = height-300;
                sensorWidth = 40;
                sensorHeight = sensorWidth*2;
                touchMargin = sensorWidth;

                canvas.drawRect(left, top, right, bottom, myPaint);



                //Here comes the code where we retrieve the connected sensors and put them in macList
                macList.add("z");

                /*xCoöList = new int[macList.size()];
                yCoöList = new int[macList.size()];*/

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
                    addSensor(xCoöList.get(i),yCoöList.get(i));
                    drawDistance(xCoöList.get(i), yCoöList.get(i));
                }

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
                            myPaint.setColor(Color.rgb(200, 200, 100));
                            addSensor(x, y);
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

    private void addSensor(int x, int y) {


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
        } else if (leftBool && bottomBool) {
            drawSensor(x,y, sensorHeight, sensorWidth);
            drawSensor(x,y-sensorWidth, sensorWidth, sensorHeight );
        } else if (rightBool && topBool) {
            drawSensor(x,y, sensorWidth, sensorHeight);
            drawSensor(x-sensorWidth,y, sensorHeight, sensorWidth);
        } else if (rightBool && bottomBool) {
            drawSensor(x-sensorWidth,y, sensorHeight, sensorWidth);
            drawSensor(x,y-sensorWidth, sensorWidth, sensorHeight);
        } else if (leftBool || rightBool) {
            if (y > top && y < bottom) {
                drawSensor(x, ySide, sensorWidth, sensorHeight);
                y = ySide;
            }
        } else if (topBool || bottomBool) {
            if (x > left && x < right) {
                drawSensor(xSide, y, sensorHeight, sensorWidth);
                x = xSide;
            }
        }

        topBool = rightBool = bottomBool = leftBool = false;

        //z = mac address of the sensor that has been selected to add.
        //TODO: check of de coo al in de arrays zitten, want dan moeten deze niet weer toegevoegd worden.
        editor = sharedPreferencesX.edit().putInt("z", x);
        editor.commit();
        editor = sharedPreferencesY.edit().putInt("z", y);
        editor.commit();

    }

    private void drawSensor(int x, int y, int sensorDrawWidth, int sensorDrawHeight) {

        Rect sensor = new Rect(x, y, x+sensorDrawWidth, y+sensorDrawHeight);
        canvas.drawRect(sensor, myPaint);
        drawingSpace.setImageBitmap(bitmap);

        toggleAdd = false;
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
        //!!!!!!!!Voor verwijderen bluetooth nodig, denk dat het op deze manier werkt maar niet zeker (linkerbovenhoek).

        //Log.e("in delete", String.valueOf(toggleDelete));
        xCoöList = yCoöList = new ArrayList<Integer>();

        xCoöList = getCoöList(sharedPreferencesX);
        yCoöList = getCoöList(sharedPreferencesY);


        myPaint.setColor(Color.rgb(255, 255, 255));
        for (int i=0; i<xCoöList.size(); i++) {
            addSensor(xCoöList.get(i),yCoöList.get(i));
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

                                    }*/
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
            addSensor(xCoöList.get(j),yCoöList.get(j));
        }

    }

    private void drawDistance(int x, int y) {
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
        path.setFillType(Path.FillType.EVEN_ODD);

        if (leftBool && topBool) {

            path.moveTo(x,y);
            path.lineTo(x-200,y);
            path.lineTo(x,y-200);
            path.lineTo(x,y);



        } else if (leftBool && bottomBool) {

        } else if (rightBool && topBool) {
            path.moveTo(x+sensorWidth,y);
            path.lineTo(x+200,y);
            path.lineTo(x+sensorWidth,y-200);
            path.lineTo(x+sensorWidth,y);
        } else if (rightBool && bottomBool) {

        } else if (leftBool || rightBool) {
            if (y > top && y < bottom) {

            }
        } else if (topBool || bottomBool) {
            if (x > left && x < right) {

            }
        }


        topBool = rightBool = bottomBool = leftBool = false;
        path.close();
        myPaint.setColor(android.graphics.Color.RED);
        myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myPaint.setAntiAlias(true);

        canvas.drawPath(path, myPaint);
        drawingSpace.setImageBitmap(bitmap);
    }
}

