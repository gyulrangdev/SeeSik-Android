package org.androidtown.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidtown.myapplication.calendar.Calendar_main;

/**
 * Created by HAMHAM on 2017-05-17.
 */

public class Evaluation extends AppCompatActivity {
    Toolbar toolbar;
    EyeThread eyeThread;
    MouthThread mouthThread;
    Calendar_main fCalendar;
    DailyEvaluation fDailyEvaluation;
    Recommend fRecommend;
    ImageSwitcher s_ludarfEye, s_ludarfMouth;
    boolean running = false;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        s_ludarfEye = (ImageSwitcher) findViewById(R.id.switcher_ludarf_eye);
        s_ludarfMouth = (ImageSwitcher) findViewById(R.id.switcher_ludarf_mouth);

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

//        s_ludarfEye.setFactory(new ViewSwitcher.ViewFactory()
//        {
//            public View makeView() {
//                ImageView imageView = new ImageView(getApplicationContext());
//                imageView.setBackgroundColor(0x00000000);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                return imageView;
//            }
//        });
//
//        s_ludarfMouth.setFactory(new ViewSwitcher.ViewFactory()
//
//        {
//            public View makeView() {
//                ImageView imageView = new ImageView(getApplicationContext());
//                imageView.setBackgroundColor(0x00000000);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                return imageView;
//            }
//        });
//        startAnimation();
    }

    private void startAnimation() {
        s_ludarfEye.setVisibility(View.VISIBLE);
        s_ludarfMouth.setVisibility(View.VISIBLE);
        eyeThread=new EyeThread();
        mouthThread = new MouthThread();
        eyeThread.start();
        mouthThread.start();
    }

    private void stopAnimation() {
        running = false;
        try {
            eyeThread.join();
            mouthThread.join();
        } catch (InterruptedException e) {
        }
        s_ludarfEye.setVisibility(View.INVISIBLE);
        s_ludarfMouth.setVisibility(View.INVISIBLE);
    }

    class EyeThread extends Thread {
        int duration = 100;
        final int imageId[] = {R.drawable.ludarf_open_eye, R.drawable.ludarf_close_eye,R.drawable.ludarf_open_eye, R.drawable.ludarf_close_eye};
        int currentIndex = 0;

        @Override
        public void run() {
            running = true;
            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            s_ludarfMouth.setImageResource(imageId[currentIndex]);
                        }
                    });
                    currentIndex++;
                    if (currentIndex == imageId.length) {
                        currentIndex = 0;
                        try {
                            int sleepTime = getRandomTime(4000, 6000);
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }
                    try {
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                    }
                }
            }   //while end

        }   //run end
    }

    class MouthThread extends Thread {

        int m_duration;
        final int image_m_Id[] = {R.drawable.ludarf_open_mouth, R.drawable.ludarf_close_mouth};
        int m_currentIndex = 0;

        @Override
        public void run() {
            running = true;
            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            s_ludarfMouth.setImageResource(image_m_Id[m_currentIndex]);
                        }
                    });
                    m_currentIndex++;
                    if (m_currentIndex == image_m_Id.length) {
                        m_currentIndex = 0;
                    }
                    try {
                        m_duration = getRandomTime(3000, 5000);
                        Thread.sleep(m_duration);
                    } catch (InterruptedException e) {
                    }
                }
            }   //while end

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAnimation();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public int getRandomTime(int min, int max) {
        return min + (int) (Math.random() * (max - min));
    }

}
