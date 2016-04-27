package com.vt.cs3714.hokienomnoms;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kelvin on 4/16/16.
 */
public class DiningHallManager {

    private final static String hallHoursURL =
            "https://secure.hosting.vt.edu/www.apps.dsa.vt.edu/hours/index.php?d=t";

    private MainActivity ma;
    private JSONObject jsonRootObject;

    private ArrayList<String> hallNamesList;

    private HashMap<String, Boolean> hallNamesStates;

    private HashMap<String, ArrayList<DiningHall>> hallsByDay;
    private ArrayList<DiningHall> currDiningHallsList;

    // Dining Dining Hall Hours AsyncTask
    private class DiningHallHoursTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            currDiningHallsList = new ArrayList<>();

            Document document = Utils.getHoursDocument(params[0], params[1], params[2]);

            if (document != null) {
                // Get the first <ul> element in the document
                Element ul = document.select("ul").first();

                // For each of the
                for (Element hallElement : ul.children()) {
                    DiningHall newHall = new DiningHall();

                    String hallWebName = hallElement.ownText();

                    // check with hallNamesStates and a hashmap?

                    for (int i = 0; i < hallNamesList.size(); i++) {
                        if (hallWebName.contains(hallNamesList.get(i)) &&
                                !hallNamesStates.get(hallNamesList.get(i))) {

                            //..............

                            JSONObject hallsObj = null;
                            try {
                                hallsObj = jsonRootObject.getJSONObject("halls");
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                hallsObj = null;
                            }

                            if (hallsObj != null) {

                                JSONObject hallObj = null;
                                try {
                                    hallObj = hallsObj.getJSONObject(hallNamesList.get(i));
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                    hallObj = null;
                                }

                                if (hallObj != null) {

                                    newHall.setHumanName(hallNamesList.get(i));

                                    newHall.setMonth(params[0]);
                                    newHall.setDay(params[1]);
                                    newHall.setYear(params[2]);

                                    try {
                                        newHall.setLocationName(hallObj.getString("locationName"));
                                        newHall.setLocationNum(hallObj.getString("locationNum"));
                                        newHall.setValidMenu(hallObj.getBoolean("validMenu"));

                                        JSONArray mealNames = hallObj.getJSONArray("mealNames");
                                        for (int j = 0; j < mealNames.length(); j++) {
                                            newHall.addMenuName(mealNames.getString(j));
                                        }

                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Element hoursDescList = hallElement.children().first();
                                    for (Element hoursDesc : hoursDescList.children()) {
                                        newHall.addHallTime(hoursDesc.ownText());
                                    }

                                    hallNamesStates.put(hallNamesList.get(i), true);
                                    currDiningHallsList.add(newHall);
                                }
                            }

                            break;

                            //..............
                        }
                        else
                        {
                            // Add as a shop to existing dining hall?
                        }
                    }
                }

                hallsByDay.put(params[0] + params[1] + params[2], currDiningHallsList);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ma.updateUI(currDiningHallsList);
        }
    }

    public DiningHallManager(MainActivity mainActivity) {
        ma = mainActivity;

        currDiningHallsList = new ArrayList<>();

        hallsByDay = new HashMap<>();

        hallNamesList = new ArrayList<>();
        hallNamesStates = new HashMap<>();

        try {
            jsonRootObject = new JSONObject(Utils.loadJSONFromAsset(ma, R.raw.dininghalldata));

            JSONArray jsonArray = jsonRootObject.getJSONArray("hallsList");

            for (int i = 0; i < jsonArray.length(); i++) {
                hallNamesList.add(jsonArray.getString(i));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            jsonRootObject = null;
        }
    }

    public ArrayList<DiningHall> getCurrDiningHallsList() {
        return currDiningHallsList;
    }

    public void gatherHallHoursData(int m, int d, int y, boolean calendarData) {

        String mon;
        if (calendarData) {
            mon = new Integer(m + 1).toString();
        }
        else {
            mon = new Integer(m).toString();
        }

        String day = new Integer(d).toString();
        String year = new Integer(y).toString();


        if (hallsByDay.containsKey(mon + day + year)) {
            currDiningHallsList = hallsByDay.get(mon + day + year);
            ma.updateUI(currDiningHallsList);
        }
        else {

            for (int i = 0; i < hallNamesList.size(); i++) {
                hallNamesStates.put(hallNamesList.get(i), false);
            }

            new DiningHallHoursTask().execute(mon, day, year);
        }
    }
}
