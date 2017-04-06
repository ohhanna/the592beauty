package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PersonalColorQ5 extends Activity {

    Button bNext;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q5);

        rg = (RadioGroup) findViewById(R.id.rdgroup);

        bNext = (Button)findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                RadioButton rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                String str_Qtype = rd.getText().toString();

                Intent intent = new Intent(getApplicationContext(), PersonalColorQ6.class);

                switch(rg.getCheckedRadioButtonId()) {
                    case R.id.radio_q5_1:
                        Toast.makeText(getApplicationContext(), str_Qtype, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;

                    case R.id.radio_q5_2:
                        Toast.makeText(getApplicationContext(), str_Qtype, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;
                }

            }
        });
    }
}