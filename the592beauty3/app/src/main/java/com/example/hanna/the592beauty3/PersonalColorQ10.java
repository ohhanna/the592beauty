package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class PersonalColorQ10 extends Activity {

    final int DIALOG_LIST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q10);

        Button button_upload = (Button) findViewById(R.id.button_upload);

        button_upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog(DIALOG_LIST);
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id){
        Log.d("test", "onCreateDialog");

        switch(id){
            case DIALOG_LIST:
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalColorQ10.this);

                final String str[] = { "TAKE A PHOTO", "ALBUM" };
                builder.setTitle("이미지를 불러올 형식을 선택하세요.").
                        setNegativeButton("CANCEL", null).setItems(str,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                if(which == 0){
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivity(intent);
                                    dismissDialog(DIALOG_LIST);
                                }
                                else if(which == 1){
                                    Intent intent2 = new Intent(Intent.ACTION_PICK);
                                    intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                    intent2.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivity(intent2);
                                    dismissDialog(DIALOG_LIST);
                                }
                            }
                        });
                return builder.create();
        }
        return super.onCreateDialog(id);
    }
}