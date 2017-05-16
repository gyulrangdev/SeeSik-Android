package org.androidtown.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.androidtown.myapplication.calendar.Calendar_main;

/**
 * Created by HAMHAM on 2017-05-17.
 */

public class Evaluation extends AppCompatActivity {
    Toolbar toolbar;

    Calendar_main fCalendar;
    DailyEvaluation fDailyEvaluation;
    Recommend fRecommend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        fCalendar = new Calendar_main();
        fDailyEvaluation = new DailyEvaluation();
        fRecommend = new Recommend();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fDailyEvaluation).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addTab(tabs.newTab().setText("일일평가"));
        tabs.addTab(tabs.newTab().setText("월별평가"));
        tabs.addTab(tabs.newTab().setText("추천음식"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("Evaluation", " 선택된 탭: " + position);

                Fragment selected = null;

                if (position == 0) {
                    selected = fDailyEvaluation;
                } else if (position == 1) {
                    selected = fCalendar;
                } else if (position == 2) {
                    selected = fRecommend;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

    }
}
