package kr.ac.suwon.it402.project.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.ac.suwon.it402.project.CalendarDBManager;
import kr.ac.suwon.it402.project.CalendarSchadule.SelectDay;
import kr.ac.suwon.it402.project.R;

/**
 * Created by ohj84_000 on 2016-08-02.
 */
public class CalendarFragment extends Fragment {

    private ArrayList<String> dayList;
    private Calendar mCal;
    private GridView gridView;
    private Button NextBtn;
    private Button PrevBtn;

    private GridAdapter gridAdapter;

    int todayMonth;
    public int showYear;
    public int showMonth;

    CalendarDBManager schaduleDB = null;

    int viewheight;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_page, container, false);


        gridView = (GridView)rootView.findViewById(R.id.gridview);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

        schaduleDB = CalendarDBManager.getInstance(getActivity());


        /*오늘에 맞춰 처음 화면 꾸려주는 부분*/


        Bundle bundleYearMonth = getArguments();
        showYear = bundleYearMonth.getInt("year");
        showMonth = bundleYearMonth.getInt("month");




        //현재 보여지는 연,월

        todayMonth=showMonth;
        dayList = new ArrayList<String>();

        mCal = Calendar.getInstance();
        mCal.set(showYear, showMonth - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //먼저 빈공간 만들어주기(요일에 맞춰야됨)
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        mCal.set(showYear, showMonth , 1);
        dayNum = 7 - mCal.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i <= dayNum; i++) {
            dayList.add("");
        }

        gridAdapter = new GridAdapter(getActivity(), dayList, viewheight);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent selectDay = new Intent(getActivity(), SelectDay.class);

                if(dayList.get(i) =="") {
                    Toast.makeText(getActivity(), "값 " +  i, Toast.LENGTH_SHORT).show();
                    return;
                }

                int day = Integer.parseInt(dayList.get(i));
                int month = showMonth;
                int year = showYear;

                selectDay.putExtra("year", year);
                selectDay.putExtra("month", month);
                selectDay.putExtra("day", day);

                startActivity(selectDay);

                //프래그먼트 교체

                Toast.makeText(getActivity(), "값 " +  day +"/"+ month +"/"+ year, Toast.LENGTH_SHORT).show();

            }
        });

        /*View.OnClickListener clickNextBtn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.preBtn:
                    {
                        if(showMonth==1){
                            showMonth=12;
                            showYear--;
                        }
                        else
                            showMonth--;

                        setGridView();
                        break;
                    }
                    case R.id.nextBtn:
                    {
                        if(showMonth==12)
                        {
                            showMonth=1;
                            showYear++;
                        }
                        else
                            showMonth++;
                        setGridView();
                        break;
                    }
                }
            }
        };

        */



        return rootView;
    }


    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }
    }


/*

    public void clickNextMonth(View v){
        switch (v.getId()){
            case R.id.preBtn:
            {
                if(showMonth==1){
                    showMonth=12;
                    showYear--;
                }
                else
                    showMonth--;

                setGridView();
                break;
            }
            case R.id.nextBtn:
            {
                if(showMonth==12)
                {
                    showMonth=1;
                    showYear++;
                }
                else
                    showMonth++;
                setGridView();
                break;
            }
        }
    }
    */

    //mainActiviey에서 값을 가져와서 받는 함수
    public void changeYearMonthByToolbar(int year, int month)
    {
        showYear = year;
        showMonth = month;

        setGridView();
    }

    public void setGridView(){
        dayList.clear();


        mCal.set(showYear, showMonth - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }

        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        mCal.set(showYear, showMonth , 1);
        dayNum = 7 - mCal.get(Calendar.DAY_OF_WEEK);

        if(dayNum != 6) {
            for (int i = 0; i <= dayNum; i++) {
                dayList.add("");
            }
        }

        gridAdapter.change();
    }


    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            int width = gridView.getWidth();
            viewheight = gridView.getHeight();

            gridAdapter.setHeight(viewheight);
            setGridView();

//리스너 제거를 위한 메소드
            removeOnGlobalLayoutListener(gridView .getViewTreeObserver(), mGlobalLayoutListener);
        }
    };

    private static void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (observer == null) {
            return ;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeGlobalOnLayoutListener(listener);
        } else {
            observer.removeOnGlobalLayoutListener(listener);
        }
    }

    public class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;
        private Calendar mCal;
        private int height, mheight;

        public void setHeight(int height)
        {
            this.height = height;


        }

        public GridAdapter(Context context, List<String> list, int height) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            this.height = height;

            if(list.size() == 35)
                mheight= height/5;
            else
                mheight = height/6;


            //000

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(list.size() == 28)
            {
                mheight = height/4;
            }
            else if(list.size() == 35)
                mheight= height/5;
            else
                mheight = height/6;

            if (convertView == null || ( (convertView.getHeight() ==0 && height != 0 )  || convertView.getHeight() != mheight ) ) {




                convertView = inflater.inflate(R.layout.oneday_layout, parent, false);
                RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.layout);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mheight);
                layout.setLayoutParams(params);



                holder = new ViewHolder();
                holder.oneDay = (TextView)convertView.findViewById(R.id.oneDay);
                holder.todayView = (RelativeLayout)convertView.findViewById(R.id.layout);
                holder.over_schadule = (TextView)convertView.findViewById(R.id.over_schadule_count);

                holder.preview_tv[0] = (TextView)convertView.findViewById(R.id.preview_schedule_item_tv_1st);
                holder.preview_tv[1] = (TextView)convertView.findViewById(R.id.preview_schedule_item_tv_2nd);
                holder.preview_tv[2] = (TextView)convertView.findViewById(R.id.preview_schedule_item_tv_3rd);
            /*데이터베이스 접근하여 스케쥴리스트 몇갠지 받아와야함*/
                //holder.scheduleCount=










                convertView.setTag(R.layout.oneday_layout, holder);
            } else {

                holder = (ViewHolder)convertView.getTag(R.layout.oneday_layout);
            }

            holder.oneDay.setText("" + getItem(position));
            //해당 날짜 텍스트 컬러,배경 변경


        /*데이터베이스 날짜에따라 스케쥴 리스트 필요
        * (스트링리스트 하나 만들어서 넣어주세요)*/

            /*for(int i=0; i<holder.scheduleCount; i++) {


            }*/

            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);

            holder.todayView.setBackgroundResource(R.drawable.xml_border_top_bottom);


            int a =((CalendarMainFrag)getParentFragment()).td_year;



            //오늘표기
            if (sToday.equals(getItem(position))) {
                if(((CalendarMainFrag)getParentFragment()).td_year == showYear && ((CalendarMainFrag)getParentFragment()).td_month == showMonth)
                    holder.todayView.setBackground(getResources().getDrawable(R.drawable.todoay_box));
            }
        /*if (td_month == month&& td_year == td_year&& sToday.equals(getItem(position).day)) { //오늘 day 텍스트 컬러 변경
            holder.rlItemGridView.setBackgroundResource(R.drawable.xml_border_today);
        }
        else
            holder.rlItemGridView.setBackgroundResource(R.drawable.xml_border);
        */

            if(getItem(position).equals(""))
                return convertView;

            Calendar mCal2;
            mCal2 = Calendar.getInstance();

            mCal2.set(showYear, showMonth-1  , Integer.parseInt( getItem(position) ) );

        /*
        if(getItem(position).haveSchedule)
            holder.checkSchedule.setVisibility(View.VISIBLE);
        else
            holder.checkSchedule.setVisibility(View.INVISIBLE);
        */

            if(mCal2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            {
                holder.oneDay.setTextColor(Color.RED);
            }

            else if(mCal2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            {
                holder.oneDay.setTextColor(Color.BLUE);
            }

            else
            {
                holder.oneDay.setTextColor(Color.GRAY);
            }



            //holder.scheduleList.setText("");
            //해당 날짜의 스케쥴리스트 출력



            if(getItem(position) != "")
            {


                String Repeat_idcode = null;

                int setday = Integer.parseInt(getItem(position));
                String[] columns2 = new String[]{"_id", "year","month","day", "date_repeat" };

                //반복있는 값들
                Cursor cReplay = schaduleDB.query(columns2, "( date_repeat LIKE '%Y%' AND year<=" + showYear + " AND month=" + showMonth + " AND day="+setday + ") OR "+
                                "( date_repeat LIKE '%M%' AND ( ( year=" + showYear + " AND month <=" + showMonth + ") OR year<" + showYear+"  ) AND day="+setday + ") OR "+
                                "( date_repeat LIKE '%W%' AND ( ( year=" + showYear + " AND month =" + showMonth + " AND day<="+setday+") OR (year=" + showYear+" AND month<" + showMonth +" ) OR year<"+showYear+" ) )"
                        ,null,null,null,null );

                if (cReplay != null) {

                    Repeat_idcode = "";
                    StringBuilder code = new StringBuilder(Repeat_idcode);

                    while (cReplay.moveToNext()) {
                        int _id = cReplay.getInt(0);

                        if(cReplay.getString(4).charAt(0) == 'W')
                        {
                            int year = cReplay.getInt(1);
                            int month = cReplay.getInt(2);
                            int day = cReplay.getInt(3);

                            Calendar tempCal = Calendar.getInstance();
                            tempCal.set(year, month,day);
                            if(tempCal.get(Calendar.DAY_OF_WEEK)==mCal2.get(Calendar.DAY_OF_WEEK)) {
                                code.append("_id=" + _id + " OR ");
                            }
                        }
                        else
                            code.append(_id + " OR ");

                    }

                    if(code.length() != 0)
                        code.append("-1");

                    Repeat_idcode = code.toString();

                    cReplay.close();
                }


                String cursor_where;

                if(Repeat_idcode.length() != 0)
                    cursor_where = " ( year=" + showYear + " AND month=" + showMonth + " AND day=" + getItem(position) + ") OR (" + Repeat_idcode +")";
                else
                    cursor_where = "year=" + showYear + " AND month=" + showMonth + " AND day=" + getItem(position);

                String[] columns = new String[]{"_id", "title","color" };

                Cursor c = schaduleDB.query(columns, cursor_where , null, null, null, null);

                if (c.getCount() != 0) {
                    if (c.getCount() >= 4) {
                        holder.over_schadule.setVisibility(View.VISIBLE);
                        holder.over_schadule.setText("+" + (c.getCount() - 3));

                        c.move(3);
                        int id = c.getInt(0);
                        c = schaduleDB.query(columns, "( (year=" + showYear + " AND month=" + showMonth + " AND day=" + getItem(position) + ") ) AND _id <= " + id, null, null, null, null);
                        // 누락 OR(" + Repeat_idcode +")
                    }

                    int i = 0;
                    while(c.moveToNext() && i != 3)
                    {
                        holder.preview_tv[i].setText(c.getString(1));
                        holder.preview_tv[i].setBackgroundColor(Color.parseColor(c.getString(2)));
                        i++;
                    }

                }
            }




            return convertView;
        }

        public void change(){
            notifyDataSetChanged();
        }

    }



}
