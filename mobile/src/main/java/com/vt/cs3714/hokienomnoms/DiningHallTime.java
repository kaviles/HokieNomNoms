package com.vt.cs3714.hokienomnoms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kelvin on 4/16/16.
 */
public class DiningHallTime {

    private static Pattern p;

    private final static String pattern =
            "[0-9]{1,2}:[0-9]{2,2}\\s*((pm)|(am)|(AM)|(PM))\\s*\\-\\s*[0-9]{1,2}:[0-9]{2,2}\\s*((pm)|(am)|(AM)|(PM))";

    private String d;
    private int[][] t;

    private Calendar open;
    private Calendar close;

    /**
     * Converts the string hour data in the description
     * to integer data and stores it in private int matrix t.
     * TODO: Decide whether to use UTC 24hr or 12hr time.
     */
    private void setIntegerHours() {

        String hoursString = extractHoursFromDescription();

        if (hoursString != null) {
            int i = 0;
            for (String timeWmeridiem : hoursString.split("-", 2)){

                String[] timeSmeridiem = timeWmeridiem.split(" ", 2);

                String[] num = timeSmeridiem[0].split(":", 2);

                t[i][0] = Integer.parseInt(num[0]);
                t[i][1] = Integer.parseInt(num[1]);

                /*
                if (timeSmeridiem[1].compareTo("am") == 0 ||
                        timeSmeridiem[1].compareTo("AM") == 0) {
                    t[i][1] +;
                }
                */

                if (timeSmeridiem[1].compareTo("pm") == 0 ||
                        timeSmeridiem[1].compareTo("PM") == 0) {
                    t[i][0] += 12;
                }

                if ((timeSmeridiem[1].compareTo("am") == 0 ||
                        timeSmeridiem[1].compareTo("am") == 0)
                        && t[i][0] == 12)
                {
                    t[i][0] -= 12;
                }

                if(i == 0)
                {
                    open.set(Calendar.HOUR_OF_DAY, t[0][0]);
                    open.set(Calendar.MINUTE, t[0][1]);
                    open.set(Calendar.SECOND, 0);
                }
                else
                {
                    close.set(Calendar.HOUR_OF_DAY, t[1][0]);
                    close.set(Calendar.MINUTE, t[1][1]);
                    close.set(Calendar.SECOND, 0);
                }

                i++;
            }
        }
        else {
            t[0][0] = 0;
            t[0][1] = 0;

            t[1][0] = 0;
            t[1][1] = 0;
        }
    }

    /**
     * Extracts the string hour data from the whole description
     * this DiningHallTime was created with.
     * @return String
     */
    private String extractHoursFromDescription() {

        Matcher m = p.matcher(d);

        String hours = null;
        if (m.find()) {
            // In future, might want to store this data in a string variable
            hours = m.group(0);
        }

        return hours;
    }

    public DiningHallTime(String hoursDescription) {

        open = Calendar.getInstance();
        close = Calendar.getInstance();

        if (p == null) {
            p = Pattern.compile(pattern);
        }

        d = hoursDescription;
        t = new int[2][2];

        setIntegerHours();
    }

    public void setYear(int year)
    {
        open.set(Calendar.YEAR, year);
        close.set(Calendar.YEAR, year);
    }

    public void setMonth(int month)
    {
        open.set(Calendar.MONTH, month - 1);
        close.set(Calendar.MONTH, month - 1);
    }

    public void setDay(int day)
    {
        open.set(Calendar.DAY_OF_MONTH, day);
        close.set(Calendar.DAY_OF_MONTH, day);

        /*
        if(t[0][0] > t[1][0])
        {
            close.set(Calendar.DAY_OF_MONTH, day );
        }
        else
        {
            close.set(Calendar.DAY_OF_MONTH, day);
        }
        */
    }

    /**
     * Get full string description this DiningHallTime was created with.
     * @return String
     */
    public String getDescription() {
        return d;
    }

    /**
     * Get the opening and closing time assoiated with this DiningHallTime.
     * @return int[2][3],
     *      int[0][x]: opening hour data.
     *      int[1][x]: closing hour data.
     *      int[x][0]: hour
     *      int[x][0]: minute
     */
    public int[][] getHourRange() {
        return t;
    }

    public Calendar getOpen()
    {
        return open;
    }

    public Calendar getClose()
    {
        return close;
    }
}