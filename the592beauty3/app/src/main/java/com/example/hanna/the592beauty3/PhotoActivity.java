package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoActivity extends Activity {
    Button button_back;
    Button button_commit;
    String photoPath;
    Bitmap bitmap;
    private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imgview = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        photoPath = intent.getStringExtra("str");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);
        bitmap = bmp;
        imgview.setImageBitmap(bmp);

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
                Intent intent = new Intent(PhotoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
