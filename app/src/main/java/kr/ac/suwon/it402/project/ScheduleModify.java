package kr.ac.suwon.it402.project;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import kr.ac.suwon.it402.project.CalendarSchadule.CustomSelectOption;
import kr.ac.suwon.it402.project.CalendarSchadule.CustomTimepicker;
import kr.ac.suwon.it402.project.Dialog.ColorSelectDialog;

public class ScheduleModify extends AppCompatActivity {


    int id;
    String[] columns;
    Cursor c;
    CalendarDBManager dbManager;

    EditText scheduleTxt;
    TextView selectDayTxt;
    TextView timeTxt;
    TextView alarmTxt;
    TextView memoTxt;
    TextView repeatTxt;
    ImageView coloring;

    DatePickerDialog mDatePickerDialog =null;
    Intent preActivity;
    Intent timepicker;
    Intent selectOption;

    int sYear;
    int sMonth;
    int sDay;

    int sHour;
    int sMinutes;

    private TextView Toolbar_center_value;
    ImageView Toolbar_left_btn, Toolbar_right_btn;
    String color;


    //현진 수정 서버 업로드
    DBManager mDBmanager= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_modify);

        Toolbar_center_value = (TextView) findViewById(R.id.toolbar_center);
        Toolbar_left_btn = (ImageView)findViewById(R.id.toolbar_left_btn);
        Toolbar_right_btn = (ImageView)findViewById(R.id.toolbar_right_btn);
        Toolbar_center_value.setText("일정");
        settingToolbarBtnImgAndListener();


        scheduleTxt=(EditText)findViewById(R.id.scheduleText);
        selectDayTxt=(TextView)findViewById(R.id.selectDayTxt);
        timeTxt=(TextView)findViewById(R.id.timeTxt);
        alarmTxt=(TextView)findViewById(R.id.alarmTxt);
        memoTxt=(TextView)findViewById(R.id.memoTxt);
        repeatTxt=(TextView)findViewById(R.id.repeatTxt);
        coloring=(ImageView)findViewById(R.id.coloring);


        //현진 수정
        mDBmanager = new DBManager(this);


        preActivity = getIntent();

        id=preActivity.getIntExtra("id",0);

        dbManager= CalendarDBManager.getInstance(this);
        columns=new String[]{"_id","year","month","day","time","title","contents","date_repeat","alert","color","owner"};
        c = dbManager.query(columns,"_id="+id,null,null,null,null);

        c.moveToNext();
        sYear=c.getInt(1);
        sMonth=c.getInt(2);
        sDay=c.getInt(3);


        scheduleTxt.setText(c.getString(5));
        selectDayTxt.setText(sYear+"."+showing(sMonth,sDay)[0]+"."+showing(sMonth,sDay)[1]);


        if(c.getString(4).equals("종일")){
            timeTxt.setText("종일");
        }
        else
        {
            sHour=Integer.parseInt((c.getString(4).split("/")[0]).split(":")[0]);
            sMinutes=Integer.parseInt((c.getString(4).split("/")[0]).split(":")[1]);

            timeTxt.setText(showing(sHour,sMinutes)[0]+":"+showing(sHour,sMinutes)[1]);
        }

        switch (c.getInt(8)){
            case -1:
                alarmTxt.setText("알람없음");
                break;
            case 1440:
                alarmTxt.setText("하루전");
                break;
            case 10080:
                alarmTxt.setText("일주일전");
                break;
            case 40440:
                alarmTxt.setText("한달전");
                break;
        }
        memoTxt.setText(c.getString(6));

        switch (c.getString(7)){
            case "NO_RE":
                repeatTxt.setText("반복없음");
                break;
            case "W/1/7":
            case "W/1/6":
            case "W/1/5":
            case "W/1/4":
            case "W/1/3":
            case "W/1/2":
            case "W/1/1":
                repeatTxt.setText("일주일마다");
                break;
            case "M/1":
                repeatTxt.setText("한달마다");
                break;
            case "Y/1":
                repeatTxt.setText("일년마다");
                break;
        }
        color=c.getString(9);
        findColorDrawable();
    }

    public void onClick(View view){
        switch(view.getId()) {
            case R.id.dateLayout: {
                final DatePickerDialog.OnDateSetListener callback= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        sYear=year;
                        sMonth=monthOfYear+1;
                        sDay=dayOfMonth;
                        selectDayTxt.setText(year+"."+showing(sMonth,sDay)[0]+"."+showing(sMonth,sDay)[1]);
                    }
                };
                mDatePickerDialog= new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT ,callback,sYear,sMonth-1,sDay);
                mDatePickerDialog.show();
                break;
            }
            case R.id.timeLayout:{
                timepicker = new Intent(this, CustomTimepicker.class);
                startActivityForResult(timepicker, 0);
                break;
            }
            case R.id.alarmLayout:{
                selectOption =  new Intent(this, CustomSelectOption.class);
                selectOption.putExtra("distinction", 1);
                startActivityForResult(selectOption,1);
                break;
            }
            case R.id.repeatLayout:{
                selectOption =  new Intent(this, CustomSelectOption.class);
                selectOption.putExtra("distinction", 2);
                startActivityForResult(selectOption,2);
                break;
            }
            case R.id.colorLayout:{
                selectOption = new Intent(this, ColorSelectDialog.class);
                selectOption.putExtra("color", color);
                startActivityForResult(selectOption,3);
                break;
            }
            case R.id.deleteBtn:{
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    //dialoginterface = 취소, 종료를 위한 인터페이스 제공 // which = 어떤 버튼 혹은 아이템을 클릭 했는지 구분
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case android.app.AlertDialog.BUTTON_POSITIVE:
                                int deleteRecordCnt = dbManager.delete("_id=" + id, null);

                                mDBmanager.delete_schadule_db(MainActivity.user_id, id);

                                Toast.makeText(ScheduleModify.this, "삭제 완료", Toast.LENGTH_LONG).show();
                                finish();
                                break;
                            case android.app.AlertDialog.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                android.app.AlertDialog mAlterDialog = builder.setTitle("일정을 삭제하시겠습니까?").
                        setPositiveButton("확인", listener).setNegativeButton("취소", listener).show();
                break;
            }
        }
    }
    //10보다 작은 숫자일때 0붙여주는 작업
    public String[] showing(int first, int second){
        String[] select = new String[2];

        if(first<10)
            select[0]="0"+first;
        else
            select[0]=""+first;

        if(second<10)
            select[1] ="0"+second;
        else
            select[1]=""+second;

        return select;
    }

    public void settingToolbarBtnImgAndListener()
    {

        Toolbar_left_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel));
        Toolbar_left_btn.setOnClickListener(Toolbar_left_btn_calender_Listener);


        Toolbar_right_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ok));

        if(Toolbar_right_btn.hasOnClickListeners())
            Toolbar_right_btn.setOnClickListener(null);

        Toolbar_right_btn.setOnClickListener(Toolbar_right_btn_calender_Listener);
           
    }


    View.OnClickListener Toolbar_left_btn_calender_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    View.OnClickListener Toolbar_right_btn_calender_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String title, time, contents ;
            int alert_int;

            ContentValues updateRowValue = new ContentValues();
            updateRowValue.put("year",sYear);
            updateRowValue.put("month", sMonth);
            updateRowValue.put("day", sDay);
            if(timeTxt.getText().toString().equals("종일"))
                time = "종일";
            else
                time = sHour +":"+ sMinutes+"/"+sHour +":"+ sMinutes;

            updateRowValue.put("time", time);

            title = scheduleTxt.getText().toString();
            updateRowValue.put("title", title);
            contents = memoTxt.getText().toString();
            if(contents.equals(""))
                contents = "내용없음";
            updateRowValue.put("contents", contents);

            String alert_repeat = repeatTxt.getText().toString();

            if(alert_repeat.equals("반복없음"))
            {
                updateRowValue.put("date_repeat", "NO_RE");
                alert_repeat = "NO_RE";
            }
            else if(alert_repeat.equals("한달마다"))
            {
                updateRowValue.put("date_repeat", "M/1");
                alert_repeat = "M/1";
            }
            else if(alert_repeat.equals("일주일마다")) {
                Calendar mCal = Calendar.getInstance();
                mCal.set(sYear,sMonth,sDay);
                alert_repeat = "W/1/"+mCal.DAY_OF_WEEK;
                updateRowValue.put("date_repeat", alert_repeat);
            }
            else {
                updateRowValue.put("date_repeat", "Y/1");
                alert_repeat = "Y/1";
            }

            String alert = alarmTxt.getText().toString();

            if(alert_repeat.equals("알람없음")) {
                updateRowValue.put("alert", -1);
                alert_int = -1;
            }
            else if(alert_repeat.equals("한달전")) {
                updateRowValue.put("alert", 40440);
                alert_int = 40440;
            }
            else if(alert_repeat.equals("일주일전")) {
                updateRowValue.put("alert", 10080);
                alert_int = 10080;
            }
            else {
                updateRowValue.put("alert", 1440);
                alert_int = 1440;
            }

            updateRowValue.put("color", color);
            updateRowValue.put("owner", "me");


            int updateRecordCnt = dbManager.update(updateRowValue,"_id="+id,null);

            mDBmanager.update_schadule_db(MainActivity.user_id, id, sYear,sMonth,sDay, title, time, contents, alert_repeat ,alert_int,color );

            Toast.makeText(ScheduleModify.this, "수정완료", Toast.LENGTH_LONG).show();
            finish();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            //timepicker
            case 0:{
                if(resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if(bundle.getBoolean("allday")){
                        timeTxt.setText("종일");
                        sHour=-1;
                        sMinutes=-1;
                    }
                    else{
                        sHour=bundle.getInt("hour");
                        sMinutes=bundle.getInt("minutes");
                        timeTxt.setText(showing(sHour,sMinutes)[0]+":"+showing(sHour,sMinutes)[1]);
                    }
                }
                break;
            }
            //알람여부
            case 1:{
                if(resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if(bundle.getBoolean("set")){
                        alarmTxt.setText("알람없음");
                    }
                    else{
                        String info=bundle.getString("data");
                        alarmTxt.setText(info);
                    }
                }
                break;
            }
            //반복여부
            case 2:{
                if(resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if(bundle.getBoolean("set")){
                        repeatTxt.setText("반복없음");
                    }
                    else{
                        String info=bundle.getString("data");
                        repeatTxt.setText(info);
                    }
                }
                break;
            }
            case 3:
                if(resultCode==RESULT_OK){
                    Bundle getcolor = data.getExtras();
                    color=getcolor.getString("color");
                    Log.i("color_To", color);

                    findColorDrawable();
                }
                else
                {
                }
                break;
        }
    }

    public void findColorDrawable()
    {
        if(color.equals("#FF78c8e6"))
        {
            coloring.setBackground(getDrawable(R.drawable.circleblue));
        }
        else if(color.equals("#ff99cc00"))
        {
            coloring.setBackground(getDrawable(R.drawable.circlegreen));
        }
        else if(color.equals("#ffff4444"))
            coloring.setBackground(getDrawable(R.drawable.circlered));
        else
            coloring.setBackground(getDrawable(R.drawable.circleyellow));

    }
}
