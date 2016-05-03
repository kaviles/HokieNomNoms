package com.vt.cs3714.hokienomnoms;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends SwipeActivity implements View.OnClickListener {

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

    private int dateCount;

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
            findViewById(R.id.btn_today).setOnClickListener(this);
        }
        catch(Exception e) {
            Log.d("MainActivity", e.toString());
            e.printStackTrace();
        }

        dateCount = 0;

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
                previous();
                break;
            case R.id.btn_nextDay:
                next();
                break;
            case R.id.btn_today:
                today();
                break;
            default:
                break;
        }
    }

    @Override
    public void previous()
    {
        calendar.add(Calendar.DATE, -1);
        dateCount--;
        prepareData();
    }

    @Override
    public void next()
    {
        calendar.add(Calendar.DATE, 1);
        dateCount++;
        prepareData();
    }

    public void today()
    {
        calendar.add(Calendar.DATE, -1*dateCount);
        dateCount = 0;
        prepareData();
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

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMMM d, y");

        tv_date.setText(sdf.format(calendar.getTime()));

        for (int i = 0; i < diningHallArrayList.size(); i++){
            DiningHall diningHall = diningHallArrayList.get(i);

            LinearLayout hallDataLayout = new LinearLayout(this);
            hallDataLayout.setOrientation(LinearLayout.HORIZONTAL);
            hallDataLayout.setOnClickListener(btn_hallClickListener);
            hallDataLayout.setTag(i);

            LinearLayout nameAndHours = new LinearLayout(this);
            nameAndHours.setOrientation(LinearLayout.VERTICAL);

            TextView tv = new TextView(this);
            tv.setText(diningHall.getHumanName());
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            nameAndHours.addView(tv);

            for (DiningHallTime time : diningHall.getDiningHallTimes()) {
                tv = new TextView(this);
                tv.setText(time.getDescription());
                tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                nameAndHours.addView(tv);
            }

            FrameLayout statusIcon = new FrameLayout(this);
            ImageView iv = new ImageView(this);

            HallStatus status = diningHall.getStatus();
            if(status != null)
            {
                switch (status)
                {
                    case OPEN:
                        iv.setImageResource(R.drawable.greenlight);
                        break;
                    case CLOSED:
                        iv.setImageResource(R.drawable.redlight);
                        break;
                    case CLOSINGSOON:
                        iv.setImageResource(R.drawable.yellowlight);
                        break;
                }
            }

            statusIcon.addView(iv);

            //Set layout params for FrameLayout containing imageview to 100x100 and add the
            // FrameLayout to the hallDataLayout (horizontal linear layout)
            FrameLayout.LayoutParams statusLayoutParams =
                    new FrameLayout.LayoutParams(100, 100);
            statusLayoutParams.gravity = Gravity.CENTER_VERTICAL; //this is not working for some reason...
            hallDataLayout.addView(statusIcon, statusLayoutParams);

            //Set layout params for the nameAndHours vertical linear layout to match x wrap,
            // set the start margin (left margin) to 20 ,and add the layout to the
            // hallDataLayout (horizontal linear layout)
            LinearLayout.LayoutParams infoLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            infoLayoutParams.setMarginStart(20);
            hallDataLayout.addView(nameAndHours, infoLayoutParams);

            //Set layout params for the hallDataLayout horizontal linear layout to match x wrap,
            // set margins to left=0, top=20, right=0, bottom=20, and add the layout to the
            // main vertical linear layout containing all current dining halls/hours
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 20);
            ll.addView(hallDataLayout, layoutParams);
        }
    }
}
