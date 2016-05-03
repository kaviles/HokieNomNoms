package com.vt.cs3714.hokienomnoms;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.Pack200;

/**
 * Created by Kelvin on 4/26/16.
 */
public class DiningHall {

    private Location loc;

    private String humanName;

    private boolean vm;
    private String locNum;
    private String locName;
    private ArrayList<String> menuNames;

    private String d;
    private String m;
    private String y;

    private ArrayList<DiningHallTime> diningHallTimes;

    public DiningHall() {
        menuNames = new ArrayList<>();
        diningHallTimes = new ArrayList<>();
    }

    public String getMonth() {
        return m;
    }

    public void setMonth(String month) {
        m = month;
    }

    public String getDay() {
        return d;
    }

    public void setDay(String day) {
        d = day;
    }

    public String getYear() {
        return y;
    }

    public void setYear(String year) {
        y = year;
    }

    public boolean hasValidMenu() {
        return vm;
    }

    public void setValidMenu(boolean flag) {
        vm = flag;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String name) {
        humanName = name;
    }

    public String getLocationName() {
        return locName;
    }

    public void setLocationName(String name) {
        locName = name;
    }

    public String getLocationNum() {
        return locNum;
    }

    public void setLocationNum(String number) {
        locNum = number;
    }

    public void setLocation(Location location) {
        loc = location;
    }

    public Location getLocation() {
        return loc;
    }

    public ArrayList<String> getMenuNamesArrayList() {
        return menuNames;
    }

    public void addMenuName(String menuName) {
        menuNames.add(menuName);
    }

    public void addHallTime(String hoursDescription) {
        DiningHallTime dht = new DiningHallTime(hoursDescription);
        dht.setYear(Integer.parseInt(y));
        dht.setMonth(Integer.parseInt(m));
        dht.setDay(Integer.parseInt(d));
        diningHallTimes.add(dht);
    }

    public ArrayList<DiningHallTime> getDiningHallTimes() {
        return diningHallTimes;
    }

    public HallStatus getStatus(Calendar cal)
    {
        HallStatus status = HallStatus.CLOSED;

        for(int i = 0; i < diningHallTimes.size(); i++)
        {
            DiningHallTime dht = diningHallTimes.get(i);

            Date openDate = dht.getOpen().getTime();
            Date closeDate = dht.getClose().getTime();
            Date curDate = cal.getTime();

            Date openDayBefore = (Date) openDate.clone();
            Date closeDayAfter = (Date) closeDate.clone();
            openDayBefore.setDate(openDayBefore.getDay() - 1);
            closeDayAfter.setDate(closeDayAfter.getDay() + 2);

            if(humanName.equals("DXpress"))
            {
                Log.d("OpenDayPefore", openDayBefore.toString());
                Log.d("Open", openDate.toString());
                Log.d("Current", curDate.toString());
                Log.d("Close", closeDate.toString());
            }

            boolean dx = humanName.equals("DXpress") && curDate.before(closeDate)
                    && curDate.after(openDayBefore);
            boolean deets = humanName.equals("Deet's Place")
                    && curDate.before(closeDayAfter) && curDate.after(openDate);

            if((curDate.before(closeDate) && curDate.after(openDate)) || dx || deets)
            {
                Date minusHour = (Date) closeDate.clone();
                minusHour.setHours(minusHour.getHours() - 1);

                if(curDate.after(minusHour) || curDate.equals(minusHour))
                {
                    status = HallStatus.CLOSINGSOON;
                }
                else
                {
                    status = HallStatus.OPEN;
                }

                if(deets)
                {
                    minusHour = (Date) closeDayAfter.clone();
                    minusHour.setHours(minusHour.getHours() - 1);

                    if(curDate.after(minusHour) || curDate.equals(minusHour))
                    {
                        status = HallStatus.CLOSINGSOON;
                    }
                    else
                    {
                        status = HallStatus.OPEN;
                    }
                }
            }
        }

        return status;
    }
}
