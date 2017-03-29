package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by nightprimula on 2017-03-29.
 */


public class PersonalColorQ4 extends Activity {

    Button button_q4;
    RadioGroup radioG_q4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q4);

        button_q4 = (Button)findViewById(R.id.button_q4);
        button_q4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PersonalColorQ5.class);
                switch(radioG_q4.getCheckedRadioButtonId()){
                    case R.id.radio_q4_1:
                        startActivity(intent);
                        break;
                    case R.id.radio_q4_2:
                        startActivity(intent);
                        break;
                    case R.id.radio_q4_3:
                        startActivity(intent);
                        break;
                    default :
                        Toast.makeText(getApplicationContext(),"버튼을 선택하세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}