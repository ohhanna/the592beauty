package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Created by nightprimula on 2017-04-13.
 */

public class Autumn extends Activity {

    Button bNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autumn);
        ColorWeight weight = (ColorWeight) getApplicationContext();
        weight.setTone("autumn");
        bNext = (Button)findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view){

                //Intent intent = new Intent(getApplicationContext(), Winter.class);
                //startActivity(intent);
            }
        });
    }
}