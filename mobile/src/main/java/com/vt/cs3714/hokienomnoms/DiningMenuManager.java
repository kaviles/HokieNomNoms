package com.vt.cs3714.hokienomnoms;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by Kelvin on 4/27/16.
 */
public class DiningMenuManager {

    private MenuActivity ma;
    private ArrayList<DiningMenu> menusList;

    ArrayList<String> mnames;
    String lname;
    String lnum;

    // Dining Dining Hall Hours AsyncTask
    private class DiningHallMenusTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            menusList = new ArrayList<>();

            for (int i = 0; i < mnames.size(); i++) {

                Document document = Utils.getDiningHallMenuDocument(lnum, lname, mnames.get(i),
                        params[0], params[1], params[2]);

                if (document != null) {
                    Element tbody = document.select("tbody").first();
                    DiningMenu newMenu = new DiningMenu(mnames.get(i));

                    if (tbody != null) {

                        DiningShop newShop = null;
                        for (Element tr : tbody.children()) {
                            if (tr.hasClass("accordion-trigger")) {
                                Element div = tr.getElementsByClass("longmenucolmenucat").first();

                                if (div != null) {
                                    String shopName = div.ownText();
                                    newShop = new DiningShop(shopName);

                                    newMenu.addShop(newShop);
                                }
                            }
                            else if (newShop != null){
                                Element ahref = tr.select("a").first();

                                if (ahref != null) {

                                    String itemName = ahref.ownText();
                                    DiningItem newItem = new DiningItem(itemName);

                                    Element div = tr.getElementsByClass("legend-icons-container").first();

                                    if (div != null) {
                                        for (Element child : div.children()) {
                                            newItem.addIngredient(child.ownText());
                                        }
                                    }

                                    newShop.addItem(newItem);
                                }
                            }
                        }

                        menusList.add(newMenu);
                    }

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ma.updateUI(menusList);
        }
    }

    public DiningMenuManager(MenuActivity MenuActivity,
                             String locnum, String locname, ArrayList<String> menuNames) {
        ma = MenuActivity;
        lnum = locnum;
        lname = locname;
        mnames = menuNames;

        menusList = new ArrayList<>();
    }

    public void gatherHallMenusData(String month, String day, String year) {

        new DiningHallMenusTask().execute(month, day, year);
    }
}
