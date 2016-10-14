package com.example.mathias.iot_parkassist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView caravan = (ImageView) findViewById(R.id.caravan);
        caravan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                float x = event.getRawX();
                float y = event.getRawY();
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

    private void addSensor(float x, float y) {


        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.caravan);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.sensor);

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(resultBitmap);


        c.drawBitmap(bitmap1, 0, 0, null);

        Paint p = new Paint();

        p.setAlpha(127);

        c.drawBitmap(bitmap2, x, y, null);

        ImageView image = (ImageView) findViewById(R.id.caravan);
        BitmapDrawable bd = new BitmapDrawable(getResources(), resultBitmap);
        image.setBackground(bd);

        // Your final bitmap is resultBitmap
    }
}
