package com.trivtech.insight;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public static String logTag = "MainActivity";

    TabLayout tabLayout;
    ViewPager2 pages;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.changeStatusBarColor(this, Color.BLACK);

        tabLayout = findViewById(R.id.tabLayout);
        pages = findViewById(R.id.pages);

        tabLayout.getLayoutParams().height = Utils.getScreenHeightPercent(.05); //5%
        pages.getLayoutParams().height = Utils.getScreenHeightPercent(.95); //95%
        pages.setClipToOutline(true); //This needs to be tested on older phones


        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        pages.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Translate"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pages.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }


}