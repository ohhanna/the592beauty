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

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_beautyphoto = (Button)findViewById(R.id.button_beautyphoto);
        button_beautyphoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                                if(which == 0){
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

                                    dismissDialog(DIALOG_LIST);
                                }

                                else if(which == 1){
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

                                    dismissDialog(DIALOG_LIST);
                                }
                            }
                        });
                return builder.create();
        }
        return super.onCreateDialog(id);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_FROM_CAMERA) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                imgview.setImageBitmap(photo);
            }
        }
        if (requestCode == PICK_FROM_GALLERY) {
            Bundle extras2 = data.getExtras();
            if (extras2 != null) {
                Bitmap photo = extras2.getParcelable("data");
                imgview.setImageBitmap(photo);
            }
        }
    }
}
