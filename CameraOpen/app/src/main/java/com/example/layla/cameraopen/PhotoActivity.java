package com.example.layla.cameraopen;

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
import java.io.IOException;

/**
 * Created by nightprimula on 2017-04-22.
 */

public class PhotoActivity extends Activity {
    Button button_back;
    Button button_commit;
    String photoPath;
    private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imgview = (ImageView) findViewById(R.id.imageView);
        ColorWeight weight = (ColorWeight) getApplicationContext();
        if (weight.getWhich() != 2) {
            Intent intent = getIntent();
            photoPath = intent.getStringExtra("str");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);
            imgview.setImageBitmap(bmp);
        }

        else if (weight.getWhich() == 2) {
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

        button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button_commit = (Button) findViewById(R.id.button_commit);
        button_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorWeight weight = (ColorWeight) getApplicationContext();
                if (weight.getWhich() == 0) {
                    Intent intent = new Intent(PhotoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intentEdit = new Intent(PhotoActivity.this, PhotoEdit.class);
                    intentEdit.putExtra("strn", photoPath);
                    startActivity(intentEdit);
                }
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
