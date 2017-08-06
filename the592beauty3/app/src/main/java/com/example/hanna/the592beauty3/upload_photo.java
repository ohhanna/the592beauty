package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.view.View;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


/**
 * Created by Layla on 2017-05-15.
 */

public class upload_photo extends Activity {
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int SPRING = 1;
    private static final int SUMMER = 2;
    private static final int AUTUMN = 3;
    private static final int WINTER = 4;

    Bitmap photo;
    ColorWeight weight;
    int result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Button seeResult = (Button)findViewById(R.id.color_result);
        Button buttonGallery = (Button) findViewById(R.id.album_open);
        Button buttonCamera = (Button) findViewById(R.id.camera_open);

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                // Gallery 호출
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // 잘라내기 셋팅
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);
                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });


        buttonCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // 카메라 호출
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                // 이미지 잘라내기 위한 크기
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });

        seeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(result==SPRING) {
                    Intent intent = new Intent(getApplicationContext(), Spring.class);
                    startActivity(intent);
                }
                if(result==SUMMER) {
                    Intent intent = new Intent(getApplicationContext(), Summer.class);
                    startActivity(intent);
                }
                if(result==AUTUMN) {
                    Intent intent = new Intent(getApplicationContext(), Autumn.class);
                    startActivity(intent);
                }
                if(result==WINTER) {
                    Intent intent = new Intent(getApplicationContext(), Winter.class);
                    startActivity(intent);
                }

            }
        });

    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        if (requestCode == PICK_FROM_CAMERA) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                photo = extras.getParcelable("data");
            }
        }
        if (requestCode == PICK_FROM_GALLERY) {
            Bundle extras2 = data.getExtras();
            if (extras2 != null) {
                photo = extras2.getParcelable("data");

            }
        }


        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        // Create a frame from the bitmap and run face detection on the frame.
        Frame frame = new Frame.Builder().setBitmap(photo).build();
        SparseArray<Face> faces = detector.detect(frame);


        //TextView textView = (TextView)findViewById(R.id.textView_eye);
        CustomView overlay = (CustomView) findViewById(R.id.customView);
        overlay.setContent(photo, faces);

        int number = overlay.setfacecolor();

        ColorWeight weight = (ColorWeight) getApplicationContext();

        int cool = weight.getCool();
        int warm = weight.getWarm();
        int w = weight.getWeight();

        if(number == 3){
            cool = cool + 3;
            warm = warm - 1;
            weight.setColor(cool, warm);
        }
        else{
            cool = cool - 1;
            warm = warm + 3;
            weight.setColor(cool, warm);
        }

       if(cool>warm) {
           if (w > 0)
                result = SUMMER;
           else
               result = WINTER;
       }
       if(warm>cool){
           if(w > 0)
               result = AUTUMN;
           else
               result = SPRING;
       }

        // Toast.makeText(getApplicationContext(), "Cool:"+weight.getCool()+" Warm:"+weight.getWarm(), Toast.LENGTH_SHORT).show();
        detector.release();
        weight.setColor(0,0);
    }

}
