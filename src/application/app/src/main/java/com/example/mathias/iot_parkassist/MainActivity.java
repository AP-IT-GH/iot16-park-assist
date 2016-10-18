package com.example.mathias.iot_parkassist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private Canvas canvas;
    final Paint myPaint = new Paint();
    private int width, height;
    private int top, left, bottom, right;
    private int sensorWidth, sensorHeight;
    private int touchMargin;
    private boolean topBool, leftBool, bottomBool, rightBool = false;
    ImageView drawingSpace;

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
                        addSensor(x, y);
                        break;
                }
                return true;
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

        myPaint.setColor(Color.rgb(200, 200, 100));


        if (x > left-touchMargin && x < left+touchMargin){
            x = left-sensorWidth;
            leftBool = true;
        } else if (x > right-touchMargin && x < right+touchMargin) {
            x = right;
            rightBool = true;
        }
        if (y > top-touchMargin && y < top+touchMargin) {
            y = top-sensorWidth;
            topBool = true;
        } else if (y > bottom-touchMargin && y < bottom+touchMargin) {
            y = bottom;
            bottomBool = true;
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
                drawSensor(x, y, sensorWidth, sensorHeight);
            }
        } else if (topBool || bottomBool) {
            if (x > left && x < right) {
                drawSensor(x, y, sensorHeight, sensorWidth);
            }
        }

        topBool = rightBool = bottomBool = leftBool = false;


    }

    private void drawSensor(int x, int y, int sensorDrawWidth, int sensorDrawHeight) {
        Rect sensor = new Rect(x, y, x+sensorDrawWidth, y+sensorDrawHeight);
        canvas.drawRect(sensor, myPaint);
        drawingSpace.setImageBitmap(bitmap);
    }
}

