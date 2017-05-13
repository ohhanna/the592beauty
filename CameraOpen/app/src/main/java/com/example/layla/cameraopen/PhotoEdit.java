package com.example.layla.cameraopen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by hanna on 2017-04-13.
 */

public class PhotoEdit extends Activity {

    Button button_save;
    Button button_back;
    Bitmap bitmap;
    String photoPath;
    private ImageView imgview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoedit);
        imgview = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        photoPath = intent.getStringExtra("strn");
        //Toast.makeText(getApplicationContext(), "path : "+photoPath, Toast.LENGTH_LONG).show();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//        Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);
//        bmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
//        Toast.makeText(getApplicationContext(), "path : "+photoPath, Toast.LENGTH_LONG).show();
//        Matrix matrix = new Matrix();
//        matrix.preRotate(90);
//
//        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//        imgview.setImageBitmap(adjustedBitmap);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , Uri.parse(photoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgview.setImageBitmap(bitmap);
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

            public void onClick(View view) {
                ColorWeight weight = (ColorWeight) getApplicationContext();
                Intent intentCommit = new Intent(PhotoEdit.this, MainActivity.class);
                intentCommit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentCommit.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentCommit);
                finish();
            }
        });
    }
}