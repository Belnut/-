package kr.ac.suwon.it402.project;

/**
 * Created by K on 2016-08-24.
 */
public class ScheduleAdd {
    private String date;
    private String time;
    private String content;
    private int s_id;

    public ScheduleAdd(String date, String time, String content, int s_id){
        this.date = date;
        this.time = time;
        this.content = content;
        this.s_id = s_id;
    }

    public String getDate(){
        return this.date;
    }

    public String getTime(){
        return this.time;
    }

    public String getContent(){
        return this.content;
    }

    public int getS_id() { return this.s_id;}
}
