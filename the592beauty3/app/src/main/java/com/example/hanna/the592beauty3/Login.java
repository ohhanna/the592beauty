package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static com.example.hanna.the592beauty3.R.id.button_home;

/**
 * Created by hanna on 2017-03-13.
 */

public class Login extends Activity {

    ImageButton button_home;
    ImageButton button_timeline;
    ImageButton button_account;

    Button button_login;
    Button button_join;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button_home = (ImageButton)findViewById(R.id.button_home);
        button_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        button_join = (Button)findViewById(R.id.button_join);
        button_join.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Join.class);
                startActivity(intent);
            }
        });


    }
}
