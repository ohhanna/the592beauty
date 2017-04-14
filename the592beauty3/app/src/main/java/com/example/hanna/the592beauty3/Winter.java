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

public class Winter extends Activity {

    Button bNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winter);

        bNext = (Button)findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view){

                //Intent intent = new Intent(getApplicationContext(), PersonalColorQ1.class);
                //startActivity(intent);
            }
        });
    }
}