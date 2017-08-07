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

                ColorWeight weight = (ColorWeight) getApplicationContext();
                int w = weight.getWeight();

                if(rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "선택하세요", Toast.LENGTH_SHORT).show();
                    return ;
                }
Intent intent = new Intent(getApplicationContext(), PersonalColorQ9.class);
                finish();
                switch(rg.getCheckedRadioButtonId()) {
                    case R.id.radio_q8_1:
                        weight.setTw(w);
                        w= w-2;
                        weight.setWeight(w);
                        Toast.makeText(getApplicationContext(), "Weight:"+weight.getWeight(), Toast.LENGTH_SHORT).show();
                        weight.setBack(0);
                        startActivity(intent);
                        break;

                    case R.id.radio_q8_2:
                        weight.setTw(w);
                        w= w+2;
                        weight.setWeight(w);
                        Toast.makeText(getApplicationContext(), "Weight:"+weight.getWeight(), Toast.LENGTH_SHORT).show();
                        weight.setBack(0);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
    public void onBackPressed() {
        ColorWeight weight = (ColorWeight) getApplicationContext();
        if(weight.getBack() == 0) {
            Intent intent = new Intent(getApplicationContext(), PersonalColorQ7.class);
            finish();
            weight.setWeight(weight.getTw());
            weight.setBack(1);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            weight.setBack(0);
            startActivity(intent);
        }
    }
}
