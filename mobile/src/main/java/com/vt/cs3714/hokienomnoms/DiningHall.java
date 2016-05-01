package com.vt.cs3714.hokienomnoms;

import java.util.ArrayList;

/**
 * Created by Kelvin on 4/26/16.
 */
public class DiningHall {

    private String humanName;

    private boolean vm;
    private String locNum;
    private String locName;
    private ArrayList<String> menuNames;

    private String d;
    private String m;
    private String y;

    private ArrayList<DiningHallTime> diningHallTimes;

    private HallStatus status;

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

    public ArrayList<String> getMenuNamesArrayList() {
        return menuNames;
    }

    public void addMenuName(String menuName) {
        menuNames.add(menuName);
    }

    public void addHallTime(String hoursDescription) {
        diningHallTimes.add(new DiningHallTime(hoursDescription));
    }

    public ArrayList<DiningHallTime> getDiningHallTimes() {
        return diningHallTimes;
    }

    public void setStatus(HallStatus status)
    {
        this.status = status;
    }

    public HallStatus getStatus()
    {
        return status;
    }
}
