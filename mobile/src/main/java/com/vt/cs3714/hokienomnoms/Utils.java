package com.vt.cs3714.hokienomnoms;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kelvin on 4/17/16.
 */
public class Utils {

    static private String hallHoursbaseURL =
            "https://secure.hosting.vt.edu/www.apps.dsa.vt.edu/hours/index.php?d=t";

    static private String hallMenusBaseURL =
            "http://foodpro.dsa.vt.edu/FoodPro.NET/longmenu.aspx";

    static public String loadJSONFromAsset(MainActivity mainActivity, int id) {

        String json = null;
        try {
            InputStream is = mainActivity.getResources().openRawResource(id);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    static public Document getHoursDocument(String month, String day, String year) {
        Document document = null;

        try {
            document = Jsoup.connect(hallHoursbaseURL)
                    .data("d_month", month)
                    .data("d_day", day)
                    .data("d_year", year)
                    .data("view", "View Date").post();
        }
        catch (IOException e) {
            e.printStackTrace();
            document = null;
        }

        return document;
    }

    static public Document getDiningHallMenuDocument(
            String locationNum, String locationName, String mealName,
            String m, String d, String y) {

        Document document = null;

        try {
            String url = hallMenusBaseURL + "?" + "sName=Virginia+Tech+Dining+Services" +
                    "&locationNum=" + locationNum + "&locationName=" + locationName +
                    "&naFlag=1" + "&dtdate=" + m + "%2f" + d + "%2f" + y + "&mealName=" + mealName;
            Connection connection = Jsoup.connect(url);

//            Connection connection = Jsoup.connect(hallMenusBaseURL)
//                    .data("sName", "Virginia+Tech+Dining+Services")
//                    .data("locationNum", locationNum)
//                    .data("locationName", locationName)
//                    .data("naFlag", "1")
//                    .data("dtdate", m + "%2f" + d + "%2f" + y) // ex: 04%2f26%2f2016
//                    .data("mealName", mealName);

            document = connection.get();
        }
        catch (IOException e) {
            e.printStackTrace();
            document = null;
        }

        return document;
    }

    static public void toast(AppCompatActivity activity, String string) {
        Toast.makeText(activity.getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

}