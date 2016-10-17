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

    ImageView caravan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        caravan = (ImageView) findViewById(R.id.caravan);
        //final View content = findViewById(android.R.id.content);
        caravan.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Remove it here unless you want to get this callback for EVERY
                //layout pass, which can get you into infinite loops if you ever
                //modify the layout from within this method.
                caravan.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                //Now you can get the width and height from content
                TextView textView = (TextView) findViewById(R.id.text);
                textView.setText(String.valueOf(caravan.getWidth()) + "  " + String.valueOf(caravan.getHeight()));
                bitmap = Bitmap.createBitmap(caravan.getWidth(), caravan.getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                caravan.setImageBitmap(bitmap);

                myPaint.setColor(Color.rgb(0, 0, 0));
                myPaint.setStrokeWidth(10);
                canvas.drawRect(300, 200, 700, 800, myPaint);
            }
        });



        //bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        /*canvas = new Canvas(bitmap);
        caravan.setImageBitmap(bitmap);

        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);
        canvas.drawRect(300, 200, 700, 800, myPaint);*/


        caravan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        addSensor(x, y);
                        //TextView textView = (TextView) findViewById(R.id.text);
                        //textView.setText("Touch coordinates : " + String.valueOf(x) + "x" + String.valueOf(y));
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


       /* Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.caravan);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.sensor);

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(resultBitmap);


        c.drawBitmap(bitmap1, 0, 0, null);

        Paint p = new Paint();

        p.setAlpha(127);

        c.drawBitmap(bitmap2, x, y, null);

        ImageView image = (ImageView) findViewById(R.id.caravan);
        BitmapDrawable bd = new BitmapDrawable(getResources(), resultBitmap);
        image.setBackground(bd);*/

        // Your final bitmap is resultBitmap

        myPaint.setColor(Color.rgb(200, 20, 100));
        Rect sensor = new Rect(x, y, x+20, y+20);
        canvas.drawRect(sensor, myPaint);
        caravan.setImageBitmap(bitmap);
    }
}
