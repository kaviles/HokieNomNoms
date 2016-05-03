package com.vt.cs3714.hokienomnoms;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Date;

public class MainActivity extends SwipeActivity implements View.OnClickListener {

    public final static String HALL_NAME = "HALL_NAME";
    public final static String DATE_TEXT = "DATE_TEXT";
    public final static String LOC_NAME = "LOC_NAME";
    public final static String LOC_NUM = "LOC_NUM";
    public final static String MONTH = "MONTH";
    public final static String DAY = "DAY";
    public final static String YEAR = "YEAR";
    public final static String MENU_NAMES = "MENU_NAMES";

    private Intent i_GPSLocServiceIntent;
    private GPSLocService mGPSLocService;
    private boolean mBound;


    private final static SimpleDateFormat sdf = new SimpleDateFormat("E, MMMM d, y");

    public final static String CAL_DAY ="CAL_DAY";
    public final static String CAL_MONTH ="CAL_MONTH";
    public final static String CAL_YEAR ="CAL_YEAR";

    private TextView tv_date;
    private TextView title;
    private LinearLayout ll;

    private Calendar calendar;
    private int today_day, today_month, today_year;

    private DiningHallManager dhm;


    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection i_GPSLocServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GPSLocService.GPSLocServiceBinder binder = (GPSLocService.GPSLocServiceBinder) service;
            mGPSLocService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mGPSLocService = null;
            mBound = false;
        }
    };

    Calendar cal;
    String todaysDate;
    Boolean dateIsToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        today_day = calendar.get(Calendar.DAY_OF_MONTH);
        today_month = calendar.get(Calendar.MONTH);
        today_year = calendar.get(Calendar.YEAR);

        tv_date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
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

        Intent intent = getIntent();
        int day = intent.getIntExtra(CAL_DAY, 0);
        int month = intent.getIntExtra(CAL_MONTH, 0);
        int year = intent.getIntExtra(CAL_YEAR, 0);
        if (year != 0) {
            calendar.set(year, month, day);
        }
        i_GPSLocServiceIntent = new Intent(this, GPSLocService.class);
        startService(i_GPSLocServiceIntent);

        i_GPSLocServiceIntent = new Intent(this, GPSLocService.class);
        bindService(i_GPSLocServiceIntent, i_GPSLocServiceConnection, Context.BIND_AUTO_CREATE);
        startService(i_GPSLocServiceIntent);


        cal = Calendar.getInstance();
        //cal.set(Calendar.HOUR_OF_DAY, 20);
        todaysDate = sdf.format(cal.getTime());

        prepareData();

        dateIsToday = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
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
        prepareData();
    }

    @Override
    public void next()
    {
        calendar.add(Calendar.DATE, 1);
        prepareData();
    }

    public void today()
    {
        calendar.set(today_year, today_month, today_day);
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

        tv_date.setText(sdf.format(calendar.getTime()));

        if(tv_date.getText().equals(todaysDate))
        {
            dateIsToday = true;
            title.setText("Open Dining Halls");
        }
        else
        {
            dateIsToday = false;
            title.setText("Dining Halls");
        }

        for (int i = 0; i < diningHallArrayList.size(); i++){
            DiningHall diningHall = diningHallArrayList.get(i);

            if(dateIsToday)
            {
                boolean open = false;
                boolean closingSoon = false;

                HallStatus status = diningHall.getStatus(cal);
                if (status != null)
                {
                    switch (status)
                    {
                        case OPEN:
                            open = true;
                            break;
                        case CLOSED:
                            open = false;
                            break;
                        case CLOSINGSOON:
                            open = true;
                            closingSoon = true;
                            break;
                    }
                }
                else
                {
                    open = false;
                }

                if(open)
                {
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

                    if(closingSoon)
                    {
                        iv.setImageResource(R.drawable.yellowlight);
                    }
                    else
                    {
                        iv.setImageResource(R.drawable.greenlight);
                    }

                    statusIcon.addView(iv);

                    //Set layout params for FrameLayout containing imageview to 50x50 and add the
                    // FrameLayout to the hallDataLayout (horizontal linear layout)
                    FrameLayout.LayoutParams statusLayoutParams =
                            new FrameLayout.LayoutParams(50, 50);
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
            else
            {
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
}