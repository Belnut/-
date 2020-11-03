package kr.ac.suwon.it402.project.CalendarSchadule;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kr.ac.suwon.it402.project.R;

/**
 * Created by 혜진 on 2016-08-17.
 */
public class SelectDayListviewAdapter extends CursorAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    public SelectDayListviewAdapter(Context context, Cursor c , int flags){
        super(context,c,flags);
        mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
    }

    class ViewHolder
            //최적화
    {
        TextView timeTxt;
        TextView infoTxt;
        ImageView imageView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemLayout = mLayoutInflater.inflate(R.layout.schedulelist_item,null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.timeTxt=(TextView)itemLayout.findViewById(R.id.timeItem);
        viewHolder.infoTxt =(TextView)itemLayout.findViewById(R.id.infoItem);
        viewHolder.imageView =(ImageView) itemLayout.findViewById(R.id.colorItem);
        itemLayout.setTag(viewHolder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String info = cursor.getString(cursor.getColumnIndex("title"));
        String color = cursor.getString(cursor.getColumnIndex("color"));

        if(time.equals("")){
            viewHolder.timeTxt.setText("종일");
        }
        else
        {
            int sHour=Integer.parseInt((time.split("/")[0]).split(":")[0]);
            int sMinutes=Integer.parseInt((time.split("/")[0]).split(":")[1]);

            viewHolder.timeTxt.setText(showing(sHour,sMinutes)[0]+":"+showing(sHour,sMinutes)[1]);
        }

        viewHolder.infoTxt.setText(info);


        //case 수정요망

        //노랑(기본색)
        if(color.equals("#ffffbb33"))
        {
            viewHolder.imageView.setBackground(mLayoutInflater.getContext().getDrawable(R.drawable.circleyellow));
        }
        //빨강
        else if(color.equals("#ffff4444"))
        {
            viewHolder.imageView.setBackground(mLayoutInflater.getContext().getDrawable(R.drawable.circlered));
        }
        //초록
        else if(color.equals("#ff99cc00")) {
            viewHolder.imageView.setBackground(mLayoutInflater.getContext().getDrawable(R.drawable.circlegreen));
        }
        //파랑
        else if( color.equals("#FF78c8e6")) {
            viewHolder.imageView.setBackground(mLayoutInflater.getContext().getDrawable(R.drawable.circleblue));
        }
        else
            ;

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
}
