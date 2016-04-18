package com.vt.cs3714.hokienomnoms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Utils utils;
    private Calendar calendar;
    private LinearLayout ll;

    private HallManager hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        utils = new Utils();
        calendar = Calendar.getInstance();

        ll  = (LinearLayout) findViewById(R.id.linearLayout);
        hm = new HallManager(this);

        try {
            findViewById(R.id.btn_prevDay).setOnClickListener(this);
            findViewById(R.id.btn_nextDay).setOnClickListener(this);
        }
        catch(Exception e) {
            Log.d("MainActivity", e.toString());
            e.printStackTrace();
        }

        prepareData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            return true;
        }
        else if (id == R.id.action_notifications) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void prepareData() {
        hm.gatherHallHoursData(
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.YEAR),
                true);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btn_prevDay:
                calendar.add(Calendar.DATE, -1);
                prepareData();
                break;
            case R.id.btn_nextDay:
                calendar.add(Calendar.DATE, 1);
                prepareData();
                break;
            default:
                break;
        }
    }

    public void updateUI(LinkedList<HallData> hallDataList) {

        ll.removeAllViews();

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMMM d y");

        TextView tv_date = (TextView) findViewById(R.id.date);
        tv_date.setText(sdf.format(calendar.getTime()));

        TextView tv;
        for (HallData data : hallDataList) {
            tv = new TextView(this);
            tv.setText(data.getName());
            ll.addView(tv);

            for (HallHour time : data.getHallHours()) {
                tv = new TextView(this);
                tv.setText(time.getDescription());
                ll.addView(tv);
            }
        }
    }
}
