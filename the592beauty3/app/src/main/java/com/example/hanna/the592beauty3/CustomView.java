package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

/**
 * Created by Layla on 2017-05-15.
 */

public class CustomView extends View{
    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;

    int lefteyeRed;
    int lefteyeGreen;
    int lefteyeBlue;

    int righteyered;
    int righteyegreen;
    int righteyeblue;

    int leftcheekred;
    int leftcheekgreen;
    int leftcheekblue;

    int rightcheekred;
    int rightcheekgreen;
    int rightcheekblue;


    public CustomView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    void setContent(Bitmap bitmap, SparseArray<Face> faces){
        mBitmap = bitmap;
        mFaces = faces;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if((mBitmap != null)&&(mFaces != null)){
            double scale = drawBitmap(canvas);
            drawFaceAnnotations(canvas, scale);
            drawFaceRectangle(canvas, scale);
        }
    }


    //그림그려줌, scale 정하기
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight/imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth*scale), (int)(imageHeight*scale));
        canvas.drawBitmap(mBitmap,null,destBounds, null);

        return scale;
    }

    //얼굴에 사각형 그려줌
    private void drawFaceRectangle(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for(int i=0; i<mFaces.size(); ++i){
            Face face = mFaces.valueAt(i);
            canvas.drawRect((float)(face.getPosition().x * scale),
                    (float)(face.getPosition().y * scale),
                    (float)((face.getPosition().x + face.getWidth())*scale),
                    (float)((face.getPosition().y + face.getHeight())*scale),
                    paint);
        }
    }

    //얼굴에 랜드마크표시 / 왼->오, 위->아래  순으로 번호 매김
    private void drawFaceAnnotations(Canvas canvas, double scale){
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setStyle(Paint.Style.FILL);

        int count = 0;

        for(int i = 0; i<mFaces.size(); ++i){
            Face face = mFaces.valueAt(i);
            for(Landmark landmark : face.getLandmarks()){
                int cx = (int)(landmark.getPosition().x * scale);
                int cy = (int)(landmark.getPosition().y * scale);
                count ++;
                if(count == 1) {
                    canvas.drawCircle(cx, cy, 10, paint);
                    int pixel = mBitmap.getPixel((int)landmark.getPosition().x, (int)landmark.getPosition().y);
                    lefteyeRed = Color.red(pixel);
                    lefteyeGreen = Color.green(pixel);
                    lefteyeBlue = Color.blue(pixel);
                }

                if(count == 2) {
                    canvas.drawCircle(cx, cy, 10, paint);
                    int pixel = mBitmap.getPixel((int)landmark.getPosition().x, (int)landmark.getPosition().y);
                    righteyered = Color.red(pixel);
                    righteyegreen = Color.green(pixel);
                    righteyeblue = Color.blue(pixel);
                }

                if(count == 3) {
                    canvas.drawCircle(cx, cy, 10, paint);
                    int pixel = mBitmap.getPixel((int)landmark.getPosition().x, (int)landmark.getPosition().y);
                    leftcheekred = Color.red(pixel);
                    leftcheekgreen = Color.green(pixel);
                    leftcheekblue= Color.blue(pixel);
                }

                if(count == 5) {
                    canvas.drawCircle(cx, cy, 10, paint);
                    int pixel = mBitmap.getPixel((int)landmark.getPosition().x, (int)landmark.getPosition().y);
                    rightcheekred = Color.red(pixel);
                    rightcheekgreen= Color.green(pixel);
                    rightcheekblue = Color.blue(pixel);
                }

                else
                    canvas.drawCircle(cx, cy, 10, paint);

            }
        }
    }

    public int setfacecolor(){
        int leftcheekyellow = 255 - leftcheekblue;
        int rightcheekyellow = 255 - rightcheekblue;
        int leftcheek;
        int rightcheek;
        int result = 3;

//y - (c + m) : c = 255-r / m = 255 - green
        leftcheek = leftcheekyellow - (255 - leftcheekred + 255 - leftcheekgreen);
        rightcheek = rightcheekyellow - (255 - rightcheekred + 255 - rightcheekgreen);

        if((leftcheek + rightcheek) / 2 < 25){
            result = -3;
        }

        return result;
    }
}


