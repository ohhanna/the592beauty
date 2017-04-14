package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by hanna on 2017-04-13.
 */

public class PhotoEdit extends Activity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;

    private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoedit);

        imgview = (ImageView) findViewById(R.id.imageView1);
        ColorWeight weight = (ColorWeight) getApplicationContext();

        if (weight.getWhich() == 0) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

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

        } else if (weight.getWhich() == 1) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_CAMERA) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                imgview.setImageBitmap(photo);
            }
        } else if (requestCode == PICK_FROM_GALLERY) {
            Uri imageUri = data.getData();
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                if(photo.getHeight() < photo.getWidth()){
                    photo = imgRotate(photo);
                }
                imgview.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), "다른사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), "다른사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            }
        }
    }
    private Bitmap imgRotate(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }
}