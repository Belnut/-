package kr.ac.suwon.it402.project.CalendarSchadule;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import kr.ac.suwon.it402.project.R;

//액티비티->다이얼로그 manifests 에서 선언하는거 잊지 말것
public class ScheduleList extends Activity {

    public int sYear;
    public int sMonth;
    public int sDay;

    public String[] scheduleInfo;
    public String[] scheduleTime;

    public ListView listView;
    CursorAdap listAdapter=null;

    //데베부분입니다 선언해주세요
    //public 데베메니져이름 dbManager = null;
    String[] columns;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulelist);

        listView=(ListView)findViewById(R.id.listView);

        Intent preActivity = getIntent();

        //선택한 연,월,일 정보로 데베접근시 사용하세요
        sYear = preActivity.getIntExtra("selectYear", 0);
        sMonth = preActivity.getIntExtra("selectMonth", 0);
        sDay = preActivity.getIntExtra("selectDay", 0);

        //dbManager = 데베메니저이름.getInstance(this);


        //columns = new String[]{"_id", "일정메모", "일정시간"};
        //c = dbManager.query(columns,null,null,null,null,null);

        //listAdapter=new CursorAdap(this, c, 0);
        listView.setAdapter(listAdapter);

        //아이템 누르면 수정 or 삭제 할수 있게 할것 (폼은 누가..???)
        //수정또는 삭제시 setResult(RESULT_OK,main); 을 하여
        //메인에서 그값을 받았을때 달력을 갱신할 예정입니다
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ScheduleList.this,"ㅇ?",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
