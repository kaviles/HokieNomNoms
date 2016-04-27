package com.vt.cs3714.hokienomnoms;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private Intent intent;

    private TextView tv_name;
    private TextView tv_date;
    private LinearLayout ll;

    private DiningMenuManager dmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        intent = getIntent();

        tv_name = (TextView) findViewById(R.id.date);
        tv_date = (TextView) findViewById(R.id.name);
        ll  = (LinearLayout) findViewById(R.id.linearLayout);

        dmm = new DiningMenuManager(this,
                intent.getStringExtra(MainActivity.LOC_NUM),
                intent.getStringExtra(MainActivity.LOC_NAME),
                intent.getStringArrayListExtra(MainActivity.MENU_NAMES));

        tv_name.setText(intent.getStringExtra(MainActivity.HALL_NAME));
        tv_date.setText(intent.getStringExtra(MainActivity.DATE_TEXT));

        prepareData();
    }

    public void prepareData() {
        dmm.gatherHallMenusData(
                intent.getStringExtra(MainActivity.MONTH),
                intent.getStringExtra(MainActivity.DAY),
                intent.getStringExtra(MainActivity.YEAR));
    }

    public void updateUI(ArrayList<DiningMenu> diningMenuArrayList) {

        ll.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView tv = null;
        for (int i = 0; i < diningMenuArrayList.size(); i++) {
            DiningMenu diningMenu = diningMenuArrayList.get(i);

            LinearLayout diningMenuLayout = new LinearLayout(this);
            diningMenuLayout.setOrientation(LinearLayout.VERTICAL);

            tv = new TextView(this);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);
            tv.setText(diningMenu.getName());

            diningMenuLayout.addView(tv);

            ArrayList<DiningShop> shopsArrayList = diningMenu.getShopsArrayList();
            for (int j = 0; j < shopsArrayList.size(); j++) {
                DiningShop diningShop = shopsArrayList.get(j);

                LinearLayout diningShopLayout = new LinearLayout(this);
                diningShopLayout.setOrientation(LinearLayout.VERTICAL);

                tv = new TextView(this);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv.setText(diningShop.getName());
                diningShopLayout.addView(tv);

                ArrayList<DiningItem> diningItemArrayList = diningShop.getItemsArrayList();
                for (int k = 0; k < diningItemArrayList.size(); k++) {
                    DiningItem diningItem = diningItemArrayList.get(k);

                    tv = new TextView(this);
                    tv.setText(diningItem.getName());
                    diningShopLayout.addView(tv);
                }

                layoutParams.setMargins(0, 5, 0, 5);
                diningMenuLayout.addView(diningShopLayout, layoutParams);
            }

            layoutParams.setMargins(0, 20, 0, 20);
            ll.addView(diningMenuLayout, layoutParams);
        }
    }
}
