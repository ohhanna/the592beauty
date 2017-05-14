package com.example.layla.practice2;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
ㄴ
import android.widget.TextView;
import android.widget.ImageView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.bb,
                options);

        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        // Create a frame from the bitmap and run face detection on the frame.
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = detector.detect(frame);

        TextView textView = (TextView)findViewById(R.id.textView_eye);
        CustomView overlay = (CustomView) findViewById(R.id.customView);
        overlay.setContent(myBitmap, faces, textView);
        detector.release();



    }
}