package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PersonalColorQ8 extends Activity {

    Button bNext;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q8);

        rg = (RadioGroup) findViewById(R.id.rdgroup);

        bNext = (Button)findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                RadioButton rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                if(rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "선택하세요", Toast.LENGTH_SHORT).show();
                    return ;
                }

                String str_Qtype = rd.getText().toString();
                Intent intent = new Intent(getApplicationContext(), PersonalColorQ9.class);

                switch(rg.getCheckedRadioButtonId()) {
                    case R.id.radio_q8_1:
                        Toast.makeText(getApplicationContext(), str_Qtype, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;

                    case R.id.radio_q8_2:
                        Toast.makeText(getApplicationContext(), str_Qtype, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;
                }

            }
        });
    }
}
