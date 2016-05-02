package com.vt.cs3714.hokienomnoms;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    private Intent intent;

    private TextView tv_name;
    private TextView tv_date;
    private LinearLayout ll;
    private TextView tv;

    private DiningMenuManager dmm;

    private ArrayList<DiningShop> shopsArrayList;
    private HashMap<Integer, ShopLayout> shopLayoutHashMap;

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

        tv = null;

        shopLayoutHashMap = new HashMap<>();

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

        for (int i = 0; i < diningMenuArrayList.size(); i++) {
            DiningMenu diningMenu = diningMenuArrayList.get(i);

            LinearLayout diningMenuLayout = new LinearLayout(this);
            diningMenuLayout.setOrientation(LinearLayout.VERTICAL);
            diningMenuLayout.setGravity(Gravity.CENTER);

            tv = new TextView(this);
            tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
            tv.setText(diningMenu.getName());
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);

            diningMenuLayout.addView(tv);

            shopsArrayList = diningMenu.getShopsArrayList();
            for (int j = 0; j < shopsArrayList.size(); j++) {
                int tag = i*10 + j;
                DiningShop diningShop = shopsArrayList.get(j);

                LinearLayout diningShopLayout = new LinearLayout(this);
                diningShopLayout.setOrientation(LinearLayout.VERTICAL);
                diningShopLayout.setGravity(Gravity.CENTER);
                diningShopLayout.setOnClickListener(btn_shopClickListener);
                diningShopLayout.setTag(tag);

                tv = new TextView(this);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv.setText(diningShop.getName());
                tv.setGravity(Gravity.CENTER);
                diningShopLayout.addView(tv);

                LinearLayout diningItemLayout = new LinearLayout(this);
                diningItemLayout.setOrientation(LinearLayout.VERTICAL);
                diningItemLayout.setGravity(Gravity.CENTER);
                ShopLayout shopLayout = new ShopLayout(diningShopLayout, diningItemLayout);
                shopLayoutHashMap.put(tag, shopLayout);

                ArrayList<DiningItem> diningItemArrayList = diningShop.getItemsArrayList();
                for (int k = 0; k < diningItemArrayList.size(); k++) {
                    DiningItem diningItem = diningItemArrayList.get(k);

                    tv = new TextView(this);
                    tv.setText(diningItem.getName());
                    tv.setGravity(Gravity.CENTER);
                    shopLayoutHashMap.get(tag).addItem(tv);
                }

                shopLayout.getItems().setVisibility(View.GONE);

                layoutParams.setMargins(0, 5, 0, 5);
                diningMenuLayout.addView(shopLayout.getName(), layoutParams);
                diningMenuLayout.addView(shopLayout.getItems());
            }

            layoutParams.setMargins(0, 20, 0, 20);
            ll.addView(diningMenuLayout, layoutParams);
        }
    }

    private View.OnClickListener btn_shopClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();

            LinearLayout items = shopLayoutHashMap.get(tag).getItems();
            items.setVisibility(items.isShown() ? View.GONE : View.VISIBLE);
        }
    };

    private class ShopLayout
    {
        private LinearLayout name, items;

        public ShopLayout(LinearLayout name, LinearLayout items)
        {
            this.name = name;
            this.items = items;
        }

        public LinearLayout getName()
        {
            return name;
        }

        public LinearLayout getItems()
        {
            return items;
        }

        public void addItem(TextView tv)
        {
            items.addView(tv);
        }
    }
}
