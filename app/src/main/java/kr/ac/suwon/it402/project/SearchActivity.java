package kr.ac.suwon.it402.project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends Activity {
    Button btnCancel;
    ClearableEditText editText;
    ListView listViewSearch;
    public static ListViewSearchAdapter adapter;
    ArrayList<ScheduleAdd> arrayList = new ArrayList<ScheduleAdd>();
    CalendarDBManager cDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //캘린더 DB
        cDB = CalendarDBManager.getInstance(this);

        String[] columns = new String[]{"_id", "year", "month", "day", "time", "title"};

        Cursor cReplay = cDB.query(columns, null, null, null, null, null);

        if(cReplay != null)
        {
            while(cReplay.moveToNext()){
                int s_id = cReplay.getInt(0);
                int year = cReplay.getInt(1);
                int month = cReplay.getInt(2);
                int day = cReplay.getInt(3);
                String time = cReplay.getString(4);
                String title = cReplay.getString(5);

                String m = Integer.toString(month);
                String d = Integer.toString(day);
                if(m.length() == 1) m = "0" + m;
                if(day < 10 && day > 0) d = "0" + d;
                if(time.equals("")) time = "하루종일";

                String date = " " + year + "-" + m + "-" + d;

                ScheduleAdd sa = new ScheduleAdd(date, time, title, s_id);
                arrayList.add(sa);
            }
        }

        listViewSearch = (ListView)findViewById(R.id.listViewSearch);
        editText = (ClearableEditText)findViewById(R.id.clearText);
        btnCancel = (Button)findViewById(R.id.buttonCancel);

        btnCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        adapter = new ListViewSearchAdapter(this, arrayList);
        listViewSearch.setAdapter(adapter);

        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l_positon) {

                TextView text = (TextView)view.findViewById(R.id.tempid);
                int id = Integer.parseInt(text.getText().toString());
                Intent modify_s = new Intent(getApplicationContext(), ScheduleModify.class);

                modify_s.putExtra("id", id);

                startActivity(modify_s);
            }
        });
    }
}
