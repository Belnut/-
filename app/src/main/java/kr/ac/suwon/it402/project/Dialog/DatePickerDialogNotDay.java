package kr.ac.suwon.it402.project.Dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

/**
 * Created by ohj84_000 on 2016-08-08.
 */
public class DatePickerDialogNotDay extends DatePickerDialog implements DatePickerDialog.OnDateSetListener {


    public DatePickerDialogNotDay(Context context, int themeResId, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context,  DatePickerDialog.THEME_HOLO_LIGHT , listener, year, monthOfYear, dayOfMonth);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int day = getContext().getResources().getIdentifier("android:id/day", null, null);
        if(day != 0){
            View dayPicker = findViewById(day);
            if(dayPicker != null){
                //Set Day view visibility Off/Gone
                dayPicker.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
    }


    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        int year = MainActivity.year;
        int month = MainActivity.month;

        /*
            Get Android DatePickerDialog without day
            - This code does not work
            THEME_DEVICE_DEFAULT_DARK
            THEME_DEVICE_DEFAULT_LIGHT
            - This code work fine
            THEME_HOLO_DARK
            THEME_HOLO_LIGHT
            THEME_TRADITIONAL
         */


    /*
        //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_HOLO_DARK,this,year, month, day){
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this,year, month-1, 1){
            //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_TRADITIONAL,this,year, month, day){
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                if(day != 0){
                    View dayPicker = findViewById(day);
                    if(dayPicker != null){
                        //Set Day view visibility Off/Gone
                        dayPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        MainActivity.year = year;
        MainActivity.month = month +1 ;

        ((MainActivity)getActivity()).ToobarSetText(year, month+1);

        ((MainActivity)getActivity()).setCalendarFragmentDateSetting();
        //Do something with the date chosen by the user
        /*
        TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        tv.setText("Date changed...");
        tv.setText(tv.getText() + "\nYear: " + year);
        tv.setText(tv.getText() + "\nMonth: " + month);

        String stringOfDate = month + "/" + year;
        tv.setText(tv.getText() + "\n\nFormatted date: " + stringOfDate);
        */
//    }


}
