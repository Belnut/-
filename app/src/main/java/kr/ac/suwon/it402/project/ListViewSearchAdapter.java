package kr.ac.suwon.it402.project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.ac.suwon.it402.project.CalendarSchadule.AddSchdule;

/**
 * Created by K on 2016-08-23.
 */
public class ListViewSearchAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<ScheduleAdd> scheduleAddList = null;
    private ArrayList<ScheduleAdd> arrayList;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    //private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewSearchAdapter(Context context, List<ScheduleAdd> scheduleAddList) {
        mContext = context;
        this.scheduleAddList = scheduleAddList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<ScheduleAdd>();
        this.arrayList.addAll(scheduleAddList);
    }

    public class ViewHolder{
        TextView date;
        TextView time;
        TextView contents;
        TextView s_id;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return scheduleAddList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item_search, null);
            // Locate the TextViews in listview_item.xml
            holder.date = (TextView) view.findViewById(R.id.textViewDate);
            holder.time = (TextView) view.findViewById(R.id.textViewST);
            holder.contents = (TextView) view.findViewById(R.id.textViewContent);
            holder.s_id = (TextView) view.findViewById(R.id.tempid);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.date.setText(scheduleAddList.get(position).getDate());
        holder.time.setText(scheduleAddList.get(position).getTime());
        holder.contents.setText(scheduleAddList.get(position).getContent());
        holder.s_id.setText(String.valueOf(scheduleAddList.get(position).getS_id()));

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        return view;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public ScheduleAdd getItem(int position) {
        return scheduleAddList.get(position) ;
    }

    //Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        scheduleAddList.clear();
        if (charText.length() == 0) {
            scheduleAddList.addAll(arrayList);
        } else {
            for (ScheduleAdd sa : arrayList) {
                if (sa.getContent().toLowerCase(Locale.getDefault()).contains(charText)){
                    scheduleAddList.add(sa);
                }
            }
        }
        notifyDataSetChanged();
    }
}
