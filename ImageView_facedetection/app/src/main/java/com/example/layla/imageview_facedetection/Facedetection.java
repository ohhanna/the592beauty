package com.example.layla.imageview_facedetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

/**
 * Created by Layla on 2017-06-17.
 */

public class Facedetection {

    SparseArray<Face> mFaces;

    public Facedetection(Context context) {
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();
    }

    double drawBitmap(Canvas canvas, Bitmap mBitmap) {

        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight/imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth*scale), (int)(imageHeight*scale));
        canvas.drawBitmap(mBitmap,null,destBounds, null);

        return scale;
    }


    void drawFaceAnnotations(Canvas canvas, double scale) {
        int count = 0;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (int i = 0; i < mFaces.size(); ++i) {
            Face face = mFaces.valueAt(i);
            for (Landmark landmark : face.getLandmarks()) {
                int cx = (int) (landmark.getPosition().x * scale);
                int cy = (int) (landmark.getPosition().y * scale);
                count++;

                canvas.drawCircle(cx, cy, 5, paint);
            }
        }
    }




}
