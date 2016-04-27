package com.vt.cs3714.hokienomnoms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String HALL_NAME = "HALL_NAME";
    public final static String DATE_TEXT = "DATE_TEXT";
    public final static String LOC_NAME = "LOC_NAME";
    public final static String LOC_NUM = "LOC_NUM";
    public final static String MONTH = "MONTH";
    public final static String DAY = "DAY";
    public final static String YEAR = "YEAR";
    public final static String MENU_NAMES = "MENU_NAMES";

    private TextView tv_date;
    private LinearLayout ll;

    private Calendar calendar;

    private DiningHallManager dhm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();

        tv_date = (TextView) findViewById(R.id.date);
        ll  = (LinearLayout) findViewById(R.id.linearLayout);

        dhm = new DiningHallManager(this);

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
        dhm.gatherHallHoursData(
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

    private View.OnClickListener btn_hallClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {

            int tag = (int) v.getTag();

            DiningHall diningHall = dhm.getCurrDiningHallsList().get(tag);

            if (diningHall.hasValidMenu()) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra(HALL_NAME, diningHall.getHumanName());
                intent.putExtra(DATE_TEXT, tv_date.getText());
                intent.putExtra(LOC_NAME, diningHall.getLocationName());
                intent.putExtra(LOC_NUM, diningHall.getLocationNum());
                intent.putExtra(MONTH, diningHall.getMonth());
                intent.putExtra(DAY, diningHall.getDay());
                intent.putExtra(YEAR, diningHall.getYear());
                intent.putExtra(MENU_NAMES, diningHall.getMenuNamesArrayList());

                startActivity(intent);
            }
            else {
                Toast toast = Toast.makeText(MainActivity.this, "DEFAULT", Toast.LENGTH_LONG);

                TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                if( tv != null) {
                    tv.setText("No menu data for\n" + diningHall.getHumanName());
                    tv.setGravity(Gravity.CENTER);
                }

                toast.show();
            }
        }
    };

    public void updateUI(ArrayList<DiningHall> diningHallArrayList) {

        ll.removeAllViews();

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMMM d y");

        tv_date.setText(sdf.format(calendar.getTime()));

        for (int i = 0; i < diningHallArrayList.size(); i++){
            DiningHall diningHall = diningHallArrayList.get(i);

            LinearLayout hallDataLayout = new LinearLayout(this);
            hallDataLayout.setOrientation(LinearLayout.VERTICAL);
            hallDataLayout.setOnClickListener(btn_hallClickListener);
            hallDataLayout.setTag(i);

            TextView tv = new TextView(this);
            tv.setText(diningHall.getHumanName());
            hallDataLayout.addView(tv);

            for (DiningHallTime time : diningHall.getDiningHallTimes()) {
                tv = new TextView(this);
                tv.setText(time.getDescription());
                hallDataLayout.addView(tv);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 10);

            ll.addView(hallDataLayout, layoutParams);
        }
    }
}
