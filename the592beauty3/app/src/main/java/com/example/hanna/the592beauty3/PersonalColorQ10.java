package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class PersonalColorQ10 extends Activity {
    final int DIALOG_LIST = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q10);

        Button button_upload = (Button) findViewById(R.id.button_upload);

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), upload_photo.class);
                startActivity(intent);
            }

        });
    }

    public void onBackPressed() {
        ColorWeight weight = (ColorWeight) getApplicationContext();
        Intent intent = new Intent(getApplicationContext(), PersonalColorQ9.class);
        finish();
        weight.setWeight(weight.getTw());
        startActivity(intent);
    }
}
