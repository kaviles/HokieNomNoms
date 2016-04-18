package com.vt.cs3714.hokienomnoms;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Kelvin on 4/16/16.
 */
public class HallManager {

    private final static String hallHoursURL =
            "https://secure.hosting.vt.edu/www.apps.dsa.vt.edu/hours/index.php?d=t";

    private MainActivity ma;

    private HashMap<String, LinkedList<HallData>> hallsHoursDays;
    private LinkedList<HallData> currHallsHoursList;

    // Dining Hall Hours AsyncTask
    private class DiningHallHoursTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                currHallsHoursList = new LinkedList<>();

                // Connect to the web site
                Document document = Jsoup.connect(hallHoursURL)
                        .data("d_month", params[0])
                        .data("d_day", params[1])
                        .data("d_year", params[2])
                        .data("view", "View Date")
                        .post();

                // Get the first <ul> element in the document
                Element ul = document.select("ul").first();

                // For each of the
                for (Element hall : ul.children()) {
                    HallData newHallData = new HallData();

                    newHallData.setName(hall.ownText());

                    Element hoursDescList = hall.children().first();
                    for (Element hoursDesc : hoursDescList.children()) {
                        newHallData.addHallHours(hoursDesc.ownText());
                    }

                    currHallsHoursList.add(newHallData);
                }

                hallsHoursDays.put(params[0] + params[1] + params[2], currHallsHoursList);

            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ma.updateUI(currHallsHoursList);
        }
    }

    public HallManager(MainActivity mainActivity) {
        ma = mainActivity;
        hallsHoursDays = new HashMap<>();
    }

    public LinkedList<HallData> getCurrentHallsHoursDataList() {
        return currHallsHoursList;
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

        if (hallsHoursDays.containsKey(mon + day + year)) {
            currHallsHoursList = hallsHoursDays.get(mon + day + year);
            ma.updateUI(currHallsHoursList);
        }
        else {
            new DiningHallHoursTask().execute(mon, day, year);
        }

    }
}
