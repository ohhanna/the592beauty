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

    Button button_q1;
    RadioGroup radioG_q1;
    RadioButton radio_q1_1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        RadioButton radio_q1_1 = (RadioButton) findViewById(R.id.radio_q1_1);
        RadioButton radio_q1_2 = (RadioButton) findViewById(R.id.radio_q1_2);

        button_q1 = (Button)findViewById(R.id.button_q1);
        button_q1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PersonalColorQ2.class);
                startActivity(intent);
//                switch(radioG_q1.getCheckedRadioButtonId()){
//                    case R.id.radio_q1_1:
//                        startActivity(intent);
//                        break;
//                    case R.id.radio_q1_2:
//                        startActivity(intent);
//                        break;
//                    default :
//                        Toast.makeText(getApplicationContext(),"버튼을 선택하세요.",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}
