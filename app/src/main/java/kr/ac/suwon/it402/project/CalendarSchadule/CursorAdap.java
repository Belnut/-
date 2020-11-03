package kr.ac.suwon.it402.project.CalendarSchadule;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import kr.ac.suwon.it402.project.R;

public class CursorAdap extends CursorAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    public CursorAdap(Context context, Cursor c , int flags){
        super(context,c,flags);
        mContext=context;
        mLayoutInflater= LayoutInflater.from(context);    }

    class ViewHolder2
            //최적화
    {
        TextView scheduleInfo;
        TextView scheduleTime;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemLayout = mLayoutInflater.inflate(R.layout.detail_schedule,null);
        ViewHolder2 viewHolder = new ViewHolder2();
        viewHolder.scheduleInfo =(TextView)itemLayout.findViewById(R.id.sInfoTxt);
        viewHolder.scheduleTime =(TextView)itemLayout.findViewById(R.id.sTimeTxt);
        itemLayout.setTag(viewHolder);

        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder2 viewHolder = (ViewHolder2)view.getTag();

        //데베에서 가져와야함!!! ""부분 채워주세요(컬럼네임이예요)
        //  String sInfo = cursor.getString(cursor.getColumnIndex(""));
        //  String sTime = cursor.getString(cursor.getColumnIndex(""));

        //viewHolder.scheduleInfo.setText(sInfo);
        //viewHolder.schduleTime.setText(sTime);
    }
}
