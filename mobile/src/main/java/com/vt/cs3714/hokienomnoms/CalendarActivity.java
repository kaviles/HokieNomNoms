package com.vt.cs3714.hokienomnoms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    CalendarView calendar;
    public final static String CAL_DAY ="CAL_DAY";
    public final static String CAL_MONTH ="CAL_MONTH";
    public final static String CAL_YEAR ="CAL_YEAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initializeCalendar();
    }

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setFirstDayOfWeek(2);

        calendar.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(CAL_DAY, dayOfMonth);
        intent.putExtra(CAL_MONTH, month);
        intent.putExtra(CAL_YEAR, year);
        startActivity(intent);
    }
}
