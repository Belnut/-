package kr.ac.suwon.it402.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ohj84_000 on 2016-08-09.
 */
public class CalendarDBManager {

    static final String DB_IDs = "MyCalender.db";
    static final String TABLE_IDS = "MyCalendarTable";
    static final int    DB_VERSION = 1;

    Context mContext = null;

    private static CalendarDBManager mCalendarDBManager = null;
    private SQLiteDatabase mCalendarDatabase = null;

    public static CalendarDBManager getInstance( Context context)
    {
        if(mCalendarDBManager == null)
        {
            mCalendarDBManager = new CalendarDBManager( context );
        }

        return mCalendarDBManager;
    }

    private CalendarDBManager(Context   context )
    {
        mCalendarDatabase = context.openOrCreateDatabase( DB_IDs , Context.MODE_PRIVATE, null);

        mCalendarDatabase.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLE_IDS +
                "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "year           INTEGER, " +
                "month          INTEGER, " +
                "day            INTEGER, " +
                "time           TEXT, " +
                "title          TEXT," +
                "contents       TEXT," +
                "date_repeat    TEXT," +
                "alert          INTEGER," +
                "color          TEXT," +
                "owner          TEXT ); ");

        //Time은 일시 설정, startHour:startMinute/endHour:endMinute 로 설정 이후 String split 함수로 두번 분리
        //date_repeat은 D(ay),W(eek),M(onth),Y(ear)로 크게 4개로 구분되며 DMY는 반복텀까지, M은 요일까지 받는다.
        // 요일은 이진법으로 계산(토~일, 거꾸로) 토 = 2^6, 수 = 2^3 일 = 2^0
        //ex) D/4 = 4일마다 한번 , M/2 = 2달마다 한번, W/2/73 = 2주, 일 수 토요일마다 알람, 해당 적용시 추가일시 요일은 고정
        //alert은 알람, 분마다 저장, -1이면 알람 없음, 차후 변경예정
        //color는 R,G,B 각각의 값을 /로 나눈 String으로 저장
        //ex) 225/225/225
        //alert, color를 제외한 위에서 언급한 요소 3개는 값이 없을시 null로 지정한다.

    }

    public long insert(ContentValues addRowValue )
    {
        return mCalendarDatabase.insert(TABLE_IDS, null, addRowValue);
    }

    public Cursor query(String[] columns,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String orderBy )
    {

        //서버연동

        return mCalendarDatabase.query(TABLE_IDS,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }

    public int update(ContentValues updateRowValue,
                      String  whereClause,
                      String[] whereArgs)
    {
        //서버연동
        return mCalendarDatabase.update(TABLE_IDS, updateRowValue, whereClause, whereArgs);
    }

    public int delete( String  whereClause, String[] whereArgs) {
        mCalendarDatabase.delete(TABLE_IDS, whereClause, whereArgs);
        //서버연동동
        return mCalendarDatabase.delete(TABLE_IDS, whereClause, whereArgs);
    }
}
