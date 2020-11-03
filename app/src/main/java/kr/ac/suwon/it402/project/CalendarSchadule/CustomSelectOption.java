package kr.ac.suwon.it402.project.CalendarSchadule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import kr.ac.suwon.it402.project.MainActivity;
import kr.ac.suwon.it402.project.R;

public class CustomSelectOption extends Activity {

    int check;
    String info;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;

    Intent main;

    Spinner spinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_select_option);

        check=0;
        main =  new Intent(this, MainActivity.class);
        Intent i = getIntent();

        radioButton1= (RadioButton)findViewById(R.id.radioButton1);
        radioButton2= (RadioButton)findViewById(R.id.radioButton2);
        spinner=(Spinner)findViewById(R.id.spinner);

        List<String> spinners = new ArrayList<String>();

        //알림여부
        if(i.getIntExtra("distinction", 0)==1){
            radioButton1.setText("알람없음");
            radioButton2.setText("알람있음");
            spinners.add("한달전");
            spinners.add("일주일전");
            spinners.add("하루전");
        }
        //반복설정
        else if(i.getIntExtra("distinction", 0)==2){
            radioButton1.setText("반복없음");
            radioButton2.setText("반복있음");
            spinners.add("일주일마다");
            spinners.add("한달마다");
            spinners.add("일년마다");
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomSelectOption.this,android.R.layout.simple_spinner_dropdown_item,spinners);
        spinner.setAdapter(adapter);

        radioGroup =(RadioGroup)findViewById(R.id.radioGroup2);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton1:{
                        spinner.setVisibility(View.GONE);
                        check=0;
                        break;
                    }
                    case R.id.radioButton2:{
                        spinner.setVisibility(View.VISIBLE);
                        check=1;
                        break;
                    }
                }
            }
        });
    }

    void onClick(View v){
        switch (v.getId()){
            case R.id.okBtn:{
                if(check==0){
                    main.putExtra("set",true);
                }
                else{
                    main.putExtra("set",false);
                    info=spinner.getSelectedItem().toString();
                    main.putExtra("data",info);
                }
                setResult(RESULT_OK,main);
                finish();
                break;
            }
            case R.id.noBtn:{
                setResult(RESULT_CANCELED,main);
                finish();
                break;
            }
        }

    }
}
