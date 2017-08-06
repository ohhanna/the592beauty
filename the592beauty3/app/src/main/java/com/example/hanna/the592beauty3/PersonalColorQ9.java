package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PersonalColorQ9 extends Activity {

    Button bNext;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q9);

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

                Intent intent = new Intent(getApplicationContext(), PersonalColorQ10.class);
                finish();
                switch(rg.getCheckedRadioButtonId()) {
                    case R.id.radio_q9_1:
                        weight.setTw(w);
                        w= w-1;
                        weight.setWeight(w);
                        Toast.makeText(getApplicationContext(), "Weight:"+weight.getWeight(), Toast.LENGTH_SHORT).show();
                        weight.setBack(0);
                        startActivity(intent);
                        break;

                    case R.id.radio_q9_2:
                        weight.setTw(w);
                        w= w+1;
                        weight.setWeight(w);
                        weight.setTw(2);
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
            Intent intent = new Intent(getApplicationContext(), PersonalColorQ8.class);
            weight.setWeight(weight.getTw());
            finish();
            weight.setBack(1);
            weight.setWeight(weight.getTw());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            weight.setBack(0);
            startActivity(intent);
        }
    }
}
