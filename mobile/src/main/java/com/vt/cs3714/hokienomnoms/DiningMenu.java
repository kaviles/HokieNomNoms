package com.vt.cs3714.hokienomnoms;

import java.util.ArrayList;

/**
 * Created by Kelvin on 4/27/16.
 */
public class DiningMenu {

    private String n;
    private ArrayList<DiningShop> shopsArrayList;

    public DiningMenu(String name) {
        n = name;

        shopsArrayList = new ArrayList<>();
    }

    public String getName() {
        return n;
    }

    public void addShop(DiningShop shop) {
        shopsArrayList.add(shop);
    }

    public ArrayList<DiningShop> getShopsArrayList() {
        return shopsArrayList;
    }
}
