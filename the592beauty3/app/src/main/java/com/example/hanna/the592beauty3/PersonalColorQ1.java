package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class PersonalColorQ1 extends Activity {

    Button bNext;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        rg = (RadioGroup) findViewById(R.id.rdgroup);

        bNext = (Button)findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view){
                RadioButton rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                ColorWeight weight = (ColorWeight) getApplicationContext();
                int cool = weight.getCool();
                int warm = weight.getWarm();


                if(rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "선택하세요", Toast.LENGTH_SHORT).show();
                    return ;
                }

                Intent intent = new Intent(getApplicationContext(), PersonalColorQ2.class);
                   switch(rg.getCheckedRadioButtonId()) {
                    case R.id.radio_q1_1:
                        weight.setTemp(cool,warm);
                        cool = cool -1;
                        warm = warm +2;
                        weight.setColor(cool, warm);
                        Toast.makeText(getApplicationContext(), "Cool:"+weight.getCool()+" Warm:"+weight.getWarm(),  Toast.LENGTH_SHORT).show();
                        weight.setBack(0);
                        startActivity(intent);
                        break;

                    case R.id.radio_q1_2:
                        weight.setTemp(cool,warm);
                        cool = cool +2;
                        warm = warm -1;
                        weight.setColor(cool, warm);
                        Toast.makeText(getApplicationContext(), "Cool:"+weight.getCool()+" Warm:"+weight.getWarm() ,  Toast.LENGTH_SHORT).show();
                        weight.setBack(0);
                        startActivity(intent);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "선택하세요", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;
                }
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }
}