package com.example.layla.imageview_facedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class MainActivity extends AppCompatActivity {
    double scale;
    int WIDTH = 20;
    int HEIGHT = 20;
    int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    float[] mVerts = new float[COUNT * 2];
    float[] mOrig = new float[COUNT * 2];
    Bitmap mBitmap;
    Matrix mMatrix = new Matrix();
    Matrix mInverse = new Matrix();

    TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.bb,
                options);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);

        //paint 색 지정
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        //임시로 쓸 비트맵과 캔버스 생성 : 캔버스에는 mybitmap을 넣음
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap,0, 0, null);

        int h;
        int w;

  /*      FaceDetector faceDetector = new
                FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
*/
        h = myBitmap.getHeight();
        w = myBitmap.getWidth();

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

        //int landmark_count = 0;
        //랜드마크 찾기
        /*
        for(int i=0; i<faces.size(); i++){
            Face face = faces.valueAt(i);
            for(Landmark landmark : face.getLandmarks()){
                int cx = (int)(landmark.getPosition().x);
                int cy = (int)(landmark.getPosition().y);
                landmark_count++;
                if(landmark_count==2) {
                    textView.setText(" x: " + cx + "y : " + cy);
                }
                tempCanvas.drawCircle(cx, cy, 5, paint);
            }
        }
*/
        imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

        tempCanvas.concat(mMatrix);
        //왼쪽눈좌표
        warp(796,321);
        tempCanvas.drawBitmapMesh(myBitmap, WIDTH, HEIGHT, mVerts, 0, null, 0, null);

    }

    //warp함수 : pixelfluid
    void warp(float cx, float cy) {
        final float K = 10000;
        float[] src = mOrig;
        float[] dst = mVerts;
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
            if (pull >= 1 ) {
                dst[i + 0] = cx;
                dst[i + 1] = cy;
            } else
                dst[i+0] = x-dx*pull;
                dst[i+1] = y-dy*pull;
                }
    }

    // XY축 정하기 : pixelfluid
    void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }
}
