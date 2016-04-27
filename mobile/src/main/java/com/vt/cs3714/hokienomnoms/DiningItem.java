package com.vt.cs3714.hokienomnoms;

import java.util.ArrayList;

/**
 * Created by Kelvin on 4/27/16.
 */
public class DiningItem {

    private String n;
    private ArrayList<String> ingredientsArrayList;

    public DiningItem(String name) {
        n = name;

        ingredientsArrayList = new ArrayList<>();
    }

    public String getName() {
        return n;
    }

    public void addIngredient(String ingredient) {
        ingredientsArrayList.add(ingredient);
    }

    public ArrayList<String> getIngredientsArrayList() {
        return ingredientsArrayList;
    }
}
