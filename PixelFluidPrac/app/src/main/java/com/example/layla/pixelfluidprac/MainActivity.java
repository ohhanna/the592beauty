package com.example.layla.pixelfluidprac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.Math;

import android.content.Context;
import android.graphics.*;
import android.view.*;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    final int WIDTH = 50;
    final int HEIGHT = 50;
    final int COUNT = (WIDTH + 1) * (HEIGHT + 1);

    final float[] mVerts = new float[COUNT * 2];
    final float[] mOrig = new float[COUNT * 2];

    final Matrix mMatrix = new Matrix();
    final Matrix mInverse = new Matrix();
    Bitmap mBitmap;
    ImageView imgView;
    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.imageView);

        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 5;
        mBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.dd, bfo);

        canvas.setBitmap(mBitmap);
        imgView.setFocusable(true);

        float w = mBitmap.getWidth();
        float h = mBitmap.getHeight();
        // construct our mesh
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = h * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = w * x / WIDTH;
                setXY(mVerts, index, fx, fy);
                setXY(mOrig, index, fx, fy);
                index += 1;
            }
        }
        mMatrix.setTranslate(10, 10);
        mMatrix.invert(mInverse);

        canvas.drawColor(0xFFCCCCCC);
        canvas.concat(mMatrix);
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, mVerts, 0, null, 0, null);

        imgView.draw(canvas);

    }


    private void warp(float cx, float cy) {
        final float K = 10000;
        float[] src = mOrig;
        float[] dst = mVerts;
        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = src[i + 0];
            float y = src[i + 1];
            float dx = cx - x;
            float dy = cy - y;
            float dd = dx * dx + dy * dy;
            float d = (float) Math.sqrt(dd);
            float pull = (K / (dd + 0.000001f));

            pull /= (d + 0.000001f);
            //   android.util.Log.d("skia", "index " + i + " dist=" + d + " pull=" + pull);

            if (pull >= 1) {
                dst[i + 0] = cx;
                dst[i + 1] = cy;
            } else {
                dst[i + 0] = x + dx * pull;
                dst[i + 1] = y + dy * pull;
            }
        }
    }

    private void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    private int mLastWarpX = -9999; // don't match a touch coordinate
    private int mLastWarpY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pt = {event.getX(), event.getY()};
        mInverse.mapPoints(pt);

        int x = (int) pt[0];
        int y = (int) pt[1];
        if (mLastWarpX != x || mLastWarpY != y) {
            mLastWarpX = x;
            mLastWarpY = y;
            warp(pt[0], pt[1]);
            imgView.invalidate();
        }
        return false;
    }

}
