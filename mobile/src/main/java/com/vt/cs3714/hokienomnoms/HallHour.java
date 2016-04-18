package com.vt.cs3714.hokienomnoms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kelvin on 4/16/16.
 */
public class HallHour {

    private static Pattern p;

    private final static String pattern =
            "[0-9]{1,2}:[0-9]{2,2}\\s*((pm)|(am)|(AM)|(PM))\\s*\\-\\s*[0-9]{1,2}:[0-9]{2,2}\\s*((pm)|(am)|(AM)|(PM))";

    private String d;
    private int[][] t;

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

                if (timeSmeridiem[1].compareTo("am") == 0 ||
                        timeSmeridiem[1].compareTo("AM") == 0) {
                    t[i][2] = 0;
                }
                else if (timeSmeridiem[1].compareTo("pm") == 0 ||
                        timeSmeridiem[1].compareTo("PM") == 0) {
                    t[i][2] = 1;
                }

                i++;
            }
        }
        else {
            t[0][0] = 0;
            t[0][1] = 0;
            t[0][2] = 0;

            t[1][0] = 0;
            t[1][1] = 0;
            t[1][2] = 0;
        }
    }

    /**
     * Extracts the string hour data from the whole description
     * this HallHour was created with.
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

    public HallHour(String hoursDescription) {

        if (p == null) {
            p = Pattern.compile(pattern);
        }

        d = hoursDescription;
        t = new int[2][3];

        setIntegerHours();
    }

    /**
     * Get full string description this HallHour was created with.
     * @return String
     */
    public String getDescription() {
        return d;
    }

    /**
     * Get the opening and closing time assoiated with this HallHour.
     * @return int[2][3],
     *      int[0][x]: opening hour data.
     *      int[1][x]: closing hour data.
     *      int[x][0]: hour
     *      int[x][0]: minute
     *      int[x][0]: meridiem, 0: am, 1: pm
     */
    public int[][] getHourRange() {
        return t;
    }

}