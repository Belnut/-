package kr.ac.suwon.it402.project.CalendarSchadule;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import kr.ac.suwon.it402.project.CalendarDBManager;
import kr.ac.suwon.it402.project.R;
import kr.ac.suwon.it402.project.ScheduleModify;

public class SelectDay extends AppCompatActivity {


    TextView daytxt;
    ListView listView;


    int year;
    int month;
    int day;

    String[] columns;
    Cursor c;
    ImageButton add_schadule_btn;

    CalendarDBManager dbManager;
    SelectDayListviewAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day);

        add_schadule_btn = (ImageButton)findViewById(R.id.add_schadule_btn);
        Intent pre = getIntent();
        Bundle bundle = pre.getExtras();
        year=bundle.getInt("year");
        month=bundle.getInt("month");
        day=bundle.getInt("day");

        daytxt=(TextView)findViewById(R.id.Day);
        daytxt.setText(year+"."+showing(month,day)[0]+"."+showing(month,day)[1]);

        listView = (ListView)findViewById(R.id.listView);

        dbManager= CalendarDBManager.getInstance(this);
        columns=new String[]{"_id","year","month","day","time","title","color","owner"};
        c = dbManager.query(columns,"year="+year+" AND month="+month+" AND day="+day ,null,null,null,null);

        Adapter = new SelectDayListviewAdapter(this,c,0);
        listView.setAdapter(Adapter);


        add_schadule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_s = new Intent(getApplicationContext(), AddSchdule.class);
                add_s.putExtra("year", year);
                add_s.putExtra("month", month);
                add_s.putExtra("day", day);

                startActivity(add_s);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent modify_s = new Intent(getApplicationContext(), ScheduleModify.class);
                int id=(int)l;
                modify_s.putExtra("id",id);
                startActivity(modify_s);
            }
        });
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


    @Override
    protected void onRestart() {
        super.onRestart();
        columns=new String[]{"_id","year","month","day","time","title","color","owner"};
        c = dbManager.query(columns,"year="+year+" AND month="+month+" AND day="+day ,null,null,null,null);
        Adapter.changeCursor(c);
    }
}