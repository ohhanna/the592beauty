package com.example.layla.pixelfluidprac;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
//이미지뷰로 바꿔야함!
//with small sample image

public class MainActivity extends Activity {
    int WIDTH = 20;
    int HEIGHT = 20;
    int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    int warpcount =0;

    float[] mVerts = new float[COUNT * 2];
    float[] mOrig = new float[COUNT * 2];
    Bitmap mBitmap;
    Matrix mMatrix = new Matrix();
    Matrix mInverse = new Matrix();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));
    }

    void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }

    private class SampleView extends View {

        public SampleView(Context context) {
            super(context);
            setFocusable(true);

            mBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.cc);

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

            //mMatrix.setTranslate(10, 10);
            mMatrix.invert(mInverse);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFCCCCCC);

            canvas.concat(mMatrix);
            canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, mVerts, 0, null, 0,
                    null);
        }
//지금이건 갸름하게효과!
        private void warp(float cx, float cy) {
            final float K = 10000;
            float[] src = mOrig;
            float[] dst = mVerts;

            if(warpcount!=0)
                src = mVerts;

            for (int i = 0; i < COUNT * 2; i += 2) {
                float x = src[i + 0];
                float y = src[i + 1];
                float dx = cx - x;
                float dy = cy - y;
                float dd = dx * dx + dy * dy;
                float d = (float)Math.sqrt(dd);
                float pull = K / (dd + 0.000001f);

                pull /= (d + 0.000001f);
                // android.util.Log.d("skia", "index " + i + " dist=" + d +
                // " pull=" + pull);

                if (pull >= 1) {
                    dst[i + 0] = cx;
                    dst[i + 1] = cy;
                } else {
                    dst[i + 0] = x + dx * pull;
                    //dst[i + 0] = x - dx * pull; 로 쓰면 확대

                    dst[i + 1] = y+ (int) 0.3 * dy * pull;
                    //dst[i + 1] = y - (int) 0.3 * dy * pull; 로 쓰면 확대

                }
            }

            warpcount++;
        }

        private int mLastWarpX = -9999; // don't match a touch coordinate
        private int mLastWarpY;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float[] pt = { event.getX(), event.getY() };
            mInverse.mapPoints(pt);

            int x = (int) pt[0];
            int y = (int) pt[1];
            if (mLastWarpX != x || mLastWarpY != y) {
                mLastWarpX = x;
                mLastWarpY = y;
                warp(pt[0], pt[1]);
                invalidate();
            }
            return true;
        }
    }
}