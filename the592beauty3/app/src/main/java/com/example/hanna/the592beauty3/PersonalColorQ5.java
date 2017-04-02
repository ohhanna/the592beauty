package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PersonalColorQ5 extends Activity {

    Button button_q5;
    RadioGroup radioG_q5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q5);

        button_q5 = (Button)findViewById(R.id.button_q5);
        button_q5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PersonalColorQ6.class);
                startActivity(intent);
//                switch(radioG_q5.getCheckedRadioButtonId()){
//                    case R.id.radio_q5_1:
//                        startActivity(intent);
//                        break;
//                    case R.id.radio_q5_2:
//                        startActivity(intent);
//                        break;
//                    default :
//                        Toast.makeText(getApplicationContext(),"버튼을 선택하세요.",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}
