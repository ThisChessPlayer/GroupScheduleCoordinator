package com.example.android.groupschedulecoordinator;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class ActivityCreateMeeting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);




    }


    public void pickTimeStart(View v)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

//        String am_pm = "";
//        if (mcurrentTime.get(Calendar.AM_PM) == Calendar.AM)
//        {
//            am_pm = "AM";
//        }
//        else if (mcurrentTime.get(Calendar.AM_PM) == Calendar.PM)
//        {
//            am_pm = "PM";
//        }
//
//        final String ampm = am_pm;
        final EditText editText = (EditText) findViewById(R.id.tbStartTime);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ActivityCreateMeeting.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.show();
    }


    public void pickTimeEnd(View v)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

//        String am_pm = "";
//        if (mcurrentTime.get(Calendar.AM_PM) == Calendar.AM)
//        {
//            am_pm = "AM";
//        }
//        else if (mcurrentTime.get(Calendar.AM_PM) == Calendar.PM)
//        {
//            am_pm = "PM";
//        }
//        final String ampm = am_pm;
        final EditText editText = (EditText) findViewById(R.id.tbEndTime);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ActivityCreateMeeting.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}