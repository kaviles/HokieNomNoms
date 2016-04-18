package com.vt.cs3714.hokienomnoms;

import java.util.LinkedList;

/**
 * Created by Kelvin on 4/16/16.
 */
public class HallData {

    private String n;
    private LinkedList<HallHour> hallHours;

    public HallData() {
        n = "";
        hallHours = new LinkedList<>();
    }

    public void setName(String name) {
        n = name;
    }

    public String getName() {
        return n;
    }

    public void addHallHours(String hoursDescription) {
        hallHours.add(new HallHour(hoursDescription));
    }

    public LinkedList<HallHour> getHallHours() {
        return hallHours;
    }
}