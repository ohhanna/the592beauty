package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

    Button button_beautyphoto;
    Button button_photoedit;
    Button button_findyourcolor;

    ImageButton button_home;
    ImageButton button_account;

    final int DIALOG_LIST = 2;

//    private static final int PICK_FROM_CAMERA = 1;
//    private static final int PICK_FROM_GALLERY = 2;
//    private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ColorWeight weight = (ColorWeight) getApplicationContext();

        button_beautyphoto = (Button)findViewById(R.id.button_beautyphoto);
        button_beautyphoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                weight.setWhich(0);
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        button_photoedit = (Button)findViewById(R.id.button_photoedit);
        button_photoedit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog(DIALOG_LIST);
            }
        });

        button_findyourcolor = (Button)findViewById(R.id.button_findyourcolor);
        button_findyourcolor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PersonalColorQ1.class);
                startActivity(intent);
            }
        });

        button_home = (ImageButton)findViewById(R.id.button_home);
        button_account = (ImageButton)findViewById(R.id.button_account);
        button_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id){
        Log.d("test", "onCreateDialog");
        switch(id){
            case DIALOG_LIST:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final String str[] = { "TAKE A PHOTO", "ALBUM" };
                builder.setTitle("이미지를 불러올 형식을 선택하세요.").
                        setNegativeButton("CANCEL", null).setItems(str,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                ColorWeight weight = (ColorWeight) getApplicationContext();
                                if(which == 0){
                                    weight.setWhich(1);
                                    dismissDialog(DIALOG_LIST);
                                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                                    startActivity(intent);
                                }
                                else if(which == 1){
                                    weight.setWhich(2);
                                    dismissDialog(DIALOG_LIST);
                                    Intent intent = new Intent(getApplicationContext(), PhotoEdit.class);
                                    startActivity(intent);
                                }
                            }
                        });
                return builder.create();
        }
        return super.onCreateDialog(id);
    }
}
