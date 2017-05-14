package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.data;

public class PhotoEdit extends Activity {
    Button button_back;
    Button button_save;
    Button button_bright;
    Button button_clear;
    Button button_crop;
    Button button_rotate;  //90도
    Button button_inverse; //좌우만
    String str;
    String photoPath;
    Bitmap bitmap;
    Bitmap adjustedBitmap;
    private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoedit);
        imgview = (ImageView) findViewById(R.id.imageView);
        ColorWeight weight = (ColorWeight) getApplicationContext();
        if (weight.getWhich() == 1) {
            Intent intent = getIntent();
            photoPath = intent.getStringExtra("str");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);
            imgview.setImageBitmap(bmp);
        } else {
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
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 2);
            } catch (ActivityNotFoundException e) {
                // Do nothing for now
            }
        }

//        button_bright = (Button) findViewById(R.id.imageButton1);
//        button_bright.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        button_clear = (Button) findViewById(R.id.imageButton2);
//        button_clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        button_crop = (Button) findViewById(R.id.imageButton3);
//        button_crop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        button_rotate = (Button) findViewById(R.id.imageButton4);
//        button_rotate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Matrix rotateMatrix = new Matrix();
//                rotateMatrix.postRotate(90); //-360~360
//                Bitmap rotateImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);
//                ((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
//                imgview.setImageBitmap(rotateImage);
//            }
//        });
//        button_inverse = (Button) findViewById(R.id.imageButton5);
//        button_inverse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Matrix sideInversion = new Matrix();
//                sideInversion.setScale(-1, 1);  // 좌우반전
//                Bitmap inverseImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), sideInversion, false);
//            }
//        });

        button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button_save = (Button) findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream outStream = null;
                try {
                    str = String.format("/sdcard/DCIM/edit/%d.jpg", System.currentTimeMillis());
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    outStream = new FileOutputStream(str);
                    adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.write(data);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PhotoEdit.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri imageUri = data.getData();
        try {
            Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            photoPath = imageUri.getPath();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            //Toast.makeText(getApplicationContext(), "path : "+photoPath, Toast.LENGTH_LONG).show();
            if (photo.getHeight() < photo.getWidth()) {
                photo = imgRotate(photo);
            }
            imgview.setImageBitmap(photo);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "다른사진을 선택해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "다른사진을 선택해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        }
    }

    private Bitmap imgRotate(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
