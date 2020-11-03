package kr.ac.suwon.it402.project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kr.ac.suwon.it402.project.CalendarSchadule.AddSchdule;
import kr.ac.suwon.it402.project.Dialog.DatePickerDialogNotDay;
import kr.ac.suwon.it402.project.fragment.CalendarFragment;
import kr.ac.suwon.it402.project.fragment.CalendarMainFrag;
import kr.ac.suwon.it402.project.fragment.Fragment2;
import kr.ac.suwon.it402.project.fragment.Fragment3;

public class MainActivity extends AppCompatActivity {

    boolean connect_status;


    //뒤로가기 버튼 종료용 변수
    private long mExitModeTime = 0L;

    //스케줄 할일 기념일 시간표 Fragment Tag
    String frag_Tag;

    //Toolbar_ceter의 값 설정
    private TextView Toolbar_center_value;


    //용도불명 임시 삭제부분
    /*
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private LinearLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    */

    //Fragment 순번
    int mCurrentFragmentIndex;

    //Fragment 순번 상수
    public final static int FRAGMENT_CALENDAR = 0;
    public final static int FRAGMENT_TWO = 1;
    public final static int FRAGMENT_THREE = 2;

    //ToolbarCenter 설정 상수
    public final static int TOOLBAR_CENTER_CALENDAR_TYPE = 0;

    //현재 다른 방법으로 사용하지 않는 변수들
    private Context contextForDialog = null;
    DatePickerDialog mDatePickerDialog = null;


    //현재 화면에서 보여지는 달력의 month, year
    public static int Amonth;
    public static int Ayear;


    ProfileActivity profile;
    //프로필 이미지뷰와 이미지

    public static ImageView profileImgV = null;
    public static Bitmap profile_bitmap = null;
    public static Bitmap photo = null;

    ImageButton search_btn;
    TextView user_name_tv;

    ImageView Toolbar_left_btn, Toolbar_right_btn;

    public static String user_id;
    static String side_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect_status = true;

        Intent getDataFromLogin = getIntent();
        user_id = getDataFromLogin.getStringExtra("id");

        profile = new ProfileActivity();
        contextForDialog = this;

        Toolbar_center_value = (TextView) findViewById(R.id.toolbar_center);
        Toolbar_left_btn = (ImageView) findViewById(R.id.toolbar_left_btn);
        Toolbar_right_btn = (ImageView) findViewById(R.id.toolbar_right_btn);


        //옆 네비게이션바의 프로필 이미지 뷰 설정
        profileImgV = (ImageView) findViewById(R.id.profile_img);
        search_btn = (ImageButton) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        //ProfileModifyActivity pf = new ProfileModifyActivity();
        user_name_tv = (TextView) findViewById(R.id.user_name);

        user_name_tv.setText(side_id);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.mtoolbar);


        long now = System.currentTimeMillis();
        final Date date = new Date(now);


        //연,월,일을 따로 저장

        SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        Ayear = Integer.parseInt(curYearFormat.format(date));
        Amonth = Integer.parseInt(curMonthFormat.format(date));

        calenderToolbarSetText(Ayear, Amonth);

        //처음시작 Fragment의 설정(일정(Calendar)로 지정)
        mCurrentFragmentIndex = FRAGMENT_CALENDAR;

        //Fragment참조용 Tag를 설정 및 Fragment의 변경 및 화면에 띄어주는 함수 실행
        frag_Tag = "calendar_frag";
        fragmentReplace(mCurrentFragmentIndex);


        setSupportActionBar(mToolbar);
        settingToolbarBtnImgAndListener(mCurrentFragmentIndex);

        setting_profile();
    }

    //Fragment 교체 함수
    public void fragmentReplace(int reqNewFragmentIndex) {
        //교체할 Fragment의 생성
        Fragment fragment;
        fragment = getFragment(reqNewFragmentIndex);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //Fragment를 넣어줄 뷰, 넣을 Fragment, Fragment 식별Tag 설정
        transaction.replace(R.id.fragment_main, fragment, frag_Tag);
        transaction.commit();
    }


    //Fragment와 TAG를 설정
    private Fragment getFragment(int idx) {
        Fragment newFragment = null;
        switch (idx) {
            case FRAGMENT_CALENDAR: //일정(Calendar) Fragment
                newFragment = new CalendarMainFrag();
                frag_Tag = "calendar_frag";
                Bundle bundleForCalendarMain = new Bundle();
                bundleForCalendarMain.putInt("year", Ayear);
                bundleForCalendarMain.putInt("month", Amonth);
                newFragment.setArguments(bundleForCalendarMain);

                changeToolbarCenter(TOOLBAR_CENTER_CALENDAR_TYPE);
                break;
            case FRAGMENT_TWO:
                newFragment = new Fragment2();
                frag_Tag = "frag2";
                break;
            case FRAGMENT_THREE:
                frag_Tag = "frag3";
                newFragment = new Fragment3();
                break;
            default:
                break;
        }

        return newFragment;
    }


    //네비게이트 버튼 클릭시 반응
    public void clickNavigateBtn(View v) {
        switch (v.getId()) {
            case R.id.fragment_calendar_btn:
                mCurrentFragmentIndex = FRAGMENT_CALENDAR;
                fragmentReplace(mCurrentFragmentIndex);
                settingToolbarBtnImgAndListener(FRAGMENT_CALENDAR);
                break;
            case R.id.fragment2_btn:
                mCurrentFragmentIndex = FRAGMENT_TWO;
                fragmentReplace(mCurrentFragmentIndex);
                settingToolbarBtnImgAndListener(FRAGMENT_TWO);
                break;
            case R.id.fragment3_btn:
                mCurrentFragmentIndex = FRAGMENT_THREE;
                fragmentReplace(mCurrentFragmentIndex);
                settingToolbarBtnImgAndListener(FRAGMENT_THREE);
                break;
            case R.id.fragment4_btn:
                //mCurrentFragmentIndex = FRAGMENT_ONE;
                //fragmentReplace(mCurrentFragmentIndex);
                break;
            default:
                break;
        }

        //NavigationBar 내려줌
        DrawerLayout d1 = (DrawerLayout) findViewById(R.id.drawer_layout);
        d1.closeDrawer(GravityCompat.START);
    }


    public void settingToolbarBtnImgAndListener(int choice) {
        switch (choice) {
            case TOOLBAR_CENTER_CALENDAR_TYPE:
                Toolbar_left_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu));
                Toolbar_left_btn.setOnClickListener(Toolbar_left_btn_calender_Listener);


                Toolbar_right_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plus));

                if (Toolbar_right_btn.hasOnClickListeners())
                    Toolbar_right_btn.setOnClickListener(null);

                Toolbar_right_btn.setOnClickListener(Toolbar_right_btn_calender_Listener);
                break;
            case FRAGMENT_TWO:
                Toolbar_left_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu));
                Toolbar_left_btn.setOnClickListener(Toolbar_left_btn_calender_Listener);


                Toolbar_right_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plus));
                if (Toolbar_right_btn.hasOnClickListeners())
                    Toolbar_right_btn.setOnClickListener(null);
                break;

            case FRAGMENT_THREE:
                Toolbar_left_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu));
                Toolbar_left_btn.setOnClickListener(Toolbar_left_btn_calender_Listener);


                Toolbar_right_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.plus));
                if (Toolbar_right_btn.hasOnClickListeners())
                    Toolbar_right_btn.setOnClickListener(null);
                break;
        }
    }

    View.OnClickListener Toolbar_left_btn_calender_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DrawerLayout d1 = (DrawerLayout) findViewById(R.id.drawer_layout);
            d1.openDrawer(GravityCompat.START);
        }
    };

    View.OnClickListener Toolbar_right_btn_calender_Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent AddForm = new Intent();
            AddForm.setClass(getApplicationContext(), AddSchdule.class);

            startActivity(AddForm);
        }
    };


    void connectOk()
    {
        connect();
        Log.i("connect result", ""+connect_status);
    }










    public  void connect()
    {
        Socket soc = new Socket();
        SocketAddress adr = new InetSocketAddress("223.195.109.37", 40);

        try
        {
            soc.connect(adr, 1000);
            if(soc.isConnected() == true)
                connect_status = true;
        }
        catch (IOException e) {
            connect_status = false;
            e.printStackTrace();
        }
    }











    //사진을 둥글게 잘라주는 함수
    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.GRAY;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    //프로필 설정
    public void setting_profile ()
    {
        //이미지 정보, 기타값 가져오는 공간
        profile_bitmap = getRoundedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.none_profile));
        profileImgV.setImageBitmap(profile_bitmap);
    }

    //toolbar center 리스너 관리
    public void changeToolbarCenter(int choice) {
        switch (choice) {
            case TOOLBAR_CENTER_CALENDAR_TYPE:
                if (Toolbar_center_value.hasOnClickListeners())
                    Toolbar_center_value.setOnClickListener(null);
                Toolbar_center_value.setOnClickListener(calendarMonthListener);
                break;

            case FRAGMENT_TWO :
            case FRAGMENT_THREE:
                if (Toolbar_center_value.hasOnClickListeners())
                    Toolbar_center_value.setOnClickListener(null);
                break;
        }
    }

    View.OnClickListener calendarMonthListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView toolbar = (TextView) view;
            String text = toolbar.getText().toString();
            String[] split = text.split("/");
            Ayear = Integer.parseInt(split[0]);
            Amonth = Integer.parseInt(split[1]);

            DatePickerDialog.OnDateSetListener datepickListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month += 1;
                    Toolbar_center_value.setText(year + "/" + month);

                    Ayear = year;
                    Amonth = month;

                    fragmentReplace(FRAGMENT_CALENDAR);
                }
            };


            //DatePickerDialog 생성 및 출력(not day, not title change)

            DatePickerDialog datePickerDialog = new DatePickerDialogNotDay(contextForDialog, 0 ,datepickListener, Ayear, Amonth-1, 1);
            datePickerDialog.setTitle("날짜 변경");
            datePickerDialog.setIcon(R.drawable.ic_change_y_m);

            datePickerDialog.show();

        }


    };

    public void calenderToolbarSetText(int mYear, int mMonth) {
        Ayear = mYear; Amonth = mMonth;
        Toolbar_center_value.setText(mYear + "/" + mMonth);
    }

    //뒤로가기 버튼 클릭시 반응
    @Override
    public void onBackPressed() {

        //Log.i("EXitMode", "" + mExitModeTime);
        if (mExitModeTime != 0 && SystemClock.uptimeMillis() - mExitModeTime < 3000) {
            moveTaskToBack(true);

            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            Toast.makeText(this, "이전키를 한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();

            mExitModeTime = SystemClock.uptimeMillis();
        }
    }


    //Toolbar값이 변했을때 CalendarFragment의 값을 변경 갱신해주기 위한 함수
    public void setCalendarFragmentDateSetting()
    {

        FragmentManager fm = getSupportFragmentManager();

        CalendarFragment frag = (CalendarFragment) fm.findFragmentByTag(frag_Tag);
        frag.changeYearMonthByToolbar(Ayear,Amonth);

    }

    public void bottom_menu_click(View v)
    {
        Intent gotoSettingClass = new Intent();
        switch (v.getId())
        {
            case R.id.setting_btn:
                gotoSettingClass.setClass(this, SettingActivity.class);
                break;

            default:
                break;

        }

        startActivity(gotoSettingClass);
    }

}
