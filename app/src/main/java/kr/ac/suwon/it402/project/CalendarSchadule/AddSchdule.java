package kr.ac.suwon.it402.project.CalendarSchadule;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.ac.suwon.it402.project.CalendarDBManager;
import kr.ac.suwon.it402.project.DBManager;
import kr.ac.suwon.it402.project.Dialog.ColorSelectDialog;
import kr.ac.suwon.it402.project.MainActivity;
import kr.ac.suwon.it402.project.R;

public class AddSchdule extends AppCompatActivity {

    Intent main;
    Intent timepicker;
    Intent selectOption;

    int selectYear;
    int selectMonth;
    int selectDay;
    int selectHour;
    int selectMinutes;

    TextView selectDayTxt;
    TextView timeTxt;
    TextView alarmTxt;
    TextView repeatTxt;
    //colorTxt는 제가 만든걸로는 적용하기 힘들어요
    //제생각에는 중요도 여부로 해서
    //만약에 중요도 표시하면 앞에다 배치할수 있는 수준으로 하는게 좋을거같아요.
    ImageView colorImg;

    String color;
    String user_id;

    EditText scheduleTxt ;
    EditText memoTxt;

    Context myActivity;

    DatePickerDialog mDatePickerDialog =null;

    boolean connect_status;


    DBManager mDBmanager = null;


    private TextView Toolbar_center_value;
    ImageView Toolbar_left_btn, Toolbar_right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schdule);

        mDBmanager = new DBManager(this);

        connect_status = true;

        myActivity = this;

        main =  new Intent(this, MainActivity.class);
        selectDayTxt=(TextView)findViewById(R.id.selectDayTxt);
        timeTxt=(TextView)findViewById(R.id.timeTxt);
        alarmTxt=(TextView)findViewById(R.id.alarmTxt);
        repeatTxt=(TextView)findViewById(R.id.repeatTxt);
        colorImg=(ImageView) findViewById(R.id.colorimg);

        scheduleTxt = (EditText)findViewById(R.id.scheduleTxt);
        memoTxt=(EditText)findViewById(R.id.memoTxt);


        Intent getdate = getIntent();




        long now = System.currentTimeMillis();
        final Date date = new Date(now);



        //연,월,일을 따로 저장

        SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        selectYear=getdate.getIntExtra("year", Integer.parseInt(curYearFormat.format(date)) );
        selectMonth=getdate.getIntExtra("month", Integer.parseInt(curMonthFormat.format(date)));
        selectDay=getdate.getIntExtra("day", Integer.parseInt(curDayFormat.format(date)));

        selectDayTxt.setText(selectYear+"."+showing(selectMonth,selectDay)[0]+"."+showing(selectMonth,selectDay)[1]);






        Toolbar_center_value = (TextView) findViewById(R.id.toolbar_center);
        Toolbar_left_btn = (ImageView)findViewById(R.id.toolbar_left_btn);
        Toolbar_right_btn = (ImageView)findViewById(R.id.toolbar_right_btn);

        Toolbar_center_value.setText("일정");

        settingToolbarBtnImgAndListener();


        color = "#ffffbb33";

        findColorDrawable();

    }

    public void settingToolbarBtnImgAndListener()
    {

        Toolbar_left_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cancel));
        Toolbar_left_btn.setOnClickListener(Toolbar_left_btn_calender_Listener);


        Toolbar_right_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ok));

        if(Toolbar_right_btn.hasOnClickListeners())
            Toolbar_right_btn.setOnClickListener(null);

        Toolbar_right_btn.setOnClickListener(Toolbar_right_btn_calender_Listener);
            /*
            case FRAGMENT_TWO :
                Toolbar_left_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu));
                Toolbar_left_btn.setOnClickListener(Toolbar_left_btn_calender_Listener);


                Toolbar_right_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plus));
                if(Toolbar_right_btn.hasOnClickListeners())
                    Toolbar_right_btn.setOnClickListener(null);
                break;

            case FRAGMENT_THREE :
                Toolbar_left_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu));
                Toolbar_left_btn.setOnClickListener(Toolbar_left_btn_calender_Listener);


                Toolbar_right_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plus));
                if(Toolbar_right_btn.hasOnClickListeners())
                    Toolbar_right_btn.setOnClickListener(null);
                break;
                */
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


            CalendarDBManager db = CalendarDBManager.getInstance(myActivity);

            String[] columns = new String[]{ "year", "month", "day", "time","title", "contents", "date_repeat", "alert" , "color", "owner"};
            ContentValues addRowValue = new ContentValues();
            addRowValue.put("year", selectYear);
            addRowValue.put("month", selectMonth);
            addRowValue.put("day", selectDay);

            if(timeTxt.getText().toString().equals("종일"))
                time = "종일";
            else
                time = selectHour +":"+ selectMinutes+"/"+selectHour +":"+ selectMinutes;

            addRowValue.put("time", time);

            title = scheduleTxt.getText().toString();
            addRowValue.put("title", title);
            contents = memoTxt.getText().toString();
            if(contents.equals(""))
                contents = "내용없음";
            addRowValue.put("contents", contents);

            String alert_repeat = repeatTxt.getText().toString();

            if(alert_repeat.equals("반복없음"))
            {
                addRowValue.put("date_repeat", "NO_RE");
                alert_repeat = "NO_RE";
            }
            else if(alert_repeat.equals("한달마다"))
            {
                addRowValue.put("date_repeat", "M/1");
                alert_repeat = "M/1";
            }
            else if(alert_repeat.equals("일주일마다")) {
                Calendar mCal = Calendar.getInstance();
                mCal.set(selectYear,selectMonth,selectDay);
                alert_repeat = "W/1/"+mCal.DAY_OF_WEEK;
                addRowValue.put("date_repeat", alert_repeat);
            }
            else {
                addRowValue.put("date_repeat", "Y/1");
                alert_repeat = "Y/1";
            }

            String alert = alarmTxt.getText().toString();

            if(alert_repeat.equals("알람없음")) {
                addRowValue.put("alert", -1);
                alert_int = -1;
            }
            else if(alert_repeat.equals("한달전")) {
                addRowValue.put("alert", 40440);
                alert_int = 40440;
            }
            else if(alert_repeat.equals("일주일전")) {
                addRowValue.put("alert", 10080);
                alert_int = 10080;
            }
            else {
                addRowValue.put("alert", 1440);
                alert_int = 1440;
            }

            addRowValue.put("color", color);
            addRowValue.put("owner", "me");



            Log.i("addschadule list", "id = " + MainActivity.user_id);
            mDBmanager.insert_schadule_db(MainActivity.user_id, (int)db.insert(addRowValue), selectYear, selectMonth, selectDay, title, time, contents, alert_repeat ,alert_int,color);


            setResult(RESULT_OK,main);
            finish();
        }
    };


    public void onClick(View view){
        switch(view.getId()) {
            case R.id.dateLayout: {
                final DatePickerDialog.OnDateSetListener callback= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectYear=year;
                        selectMonth=monthOfYear+1;
                        selectDay=dayOfMonth;
                        selectDayTxt.setText(selectYear+"."+showing(selectMonth,selectDay)[0]+"."+showing(selectMonth,selectDay)[1]);
                    }
                };
                mDatePickerDialog= new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT ,callback,selectYear,selectMonth-1,selectDay);
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
            case R.id.toolbar_left_btn:{
                setResult(RESULT_CANCELED,main);
                this.finish();
                break;
            }
            case R.id.toolbar_right_btn:{
                /*DB에 저장*/

            }


        }
    }

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
                        selectHour=-1;
                        selectMinutes=-1;
                    }
                    else{
                        selectHour=bundle.getInt("hour");
                        selectMinutes=bundle.getInt("minutes");
                        timeTxt.setText(showing(selectHour,selectMinutes)[0]+":"+showing(selectHour,selectMinutes)[1]);
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
    public void findColorDrawable()
    {
        if(color.equals("#FF78c8e6"))
        {
            colorImg.setBackground(getDrawable(R.drawable.circleblue));
        }
        else if(color.equals("#ff99cc00"))
        {
            colorImg.setBackground(getDrawable(R.drawable.circlegreen));
        }
        else if(color.equals("#ffff4444"))
            colorImg.setBackground(getDrawable(R.drawable.circlered));
        else
            colorImg.setBackground(getDrawable(R.drawable.circleyellow));

    }



    private class CheckServerTask extends AsyncTask<Integer, Integer, Integer>
    {

        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(Integer... arg0) {
            request("http://223.195.109.37");
            return null;
        }

        protected  void onPostExecute(Integer a) {
        }
    }

    private void request(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            connect_status = true;
                            break;
                        }
                    }

                    reader.close();
                    conn.disconnect();
                }
            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            connect_status = false;
            ex.printStackTrace();
        }
        return ;
    }

}
