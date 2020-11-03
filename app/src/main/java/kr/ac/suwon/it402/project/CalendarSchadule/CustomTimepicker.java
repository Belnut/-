package kr.ac.suwon.it402.project.CalendarSchadule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import kr.ac.suwon.it402.project.MainActivity;
import kr.ac.suwon.it402.project.R;

public class CustomTimepicker extends Activity {

    int check;
    RadioGroup radioGroup;
    TimePicker timePicker ;

    Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_timepicker);

        check=0;

        main =  new Intent(this, MainActivity.class);
        timePicker=(TimePicker)findViewById(R.id.timePicker);

        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(0);

        radioGroup =(RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.alldayRadio:{
                        timePicker.setVisibility(View.GONE);
                        check=0;
                        break;
                    }
                    case R.id.timeRadio:{
                        timePicker.setVisibility(View.VISIBLE);
                        check=1;
                        break;
                    }
                }
            }
        });
    }
    void onClick(View view){
        switch (view.getId()){
            case R.id.okBtn:{
                if(check==0){
                    main.putExtra("allday",true);
                }
                else{
                    main.putExtra("allday",false);
                    main.putExtra("hour",timePicker.getCurrentHour());
                    main.putExtra("minutes",timePicker.getCurrentMinute());
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
