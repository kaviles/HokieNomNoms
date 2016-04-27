package com.vt.cs3714.hokienomnoms;

import java.util.ArrayList;

/**
 * Created by Kelvin on 4/27/16.
 */
public class DiningShop {

    private String n;
    private ArrayList<DiningItem> itemsArrayList;

    public DiningShop(String name) {
        n = name;

        itemsArrayList = new ArrayList<>();
    }

    public String getName() {
        return n;
    }

    public void addItem(DiningItem item) {
        itemsArrayList.add(item);
    }

    public ArrayList<DiningItem> getItemsArrayList() {
        return itemsArrayList;
    }
}
