package org.androidtown.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    ImageButton nurseBtn;
    ImageView nurseView;
    ImageButton evaBtn;
    ImageButton selectDietBtn;
    Handler mHandler = new Handler();
    ImageThread thread;
    ImageSwitcher switcher;
    boolean running;

    static DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBase(MainActivity.this);
        evaBtn = (ImageButton) findViewById(R.id.evaluationBtn);
        selectDietBtn = (ImageButton) findViewById(R.id.selectDietBtn);
        switcher = (ImageSwitcher) findViewById(R.id.switcher);
        nurseBtn = (ImageButton) findViewById(R.id.nurse);
        nurseView = (ImageView) findViewById(R.id.nurseWear);
        final Animation ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.click_animation);


        switcher.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.clearAnimation();
              switcher.startAnimation(ani);
            }
        });

        nurseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(nurseView.getVisibility()==View.INVISIBLE)
                  nurseView.setVisibility(View.VISIBLE);
                else
                    nurseView.setVisibility(View.INVISIBLE);
            }
        });


        evaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.clearAnimation();
                evaBtn.startAnimation(ani);
                Intent intent = new Intent(getApplicationContext(), Evaluation.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        selectDietBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.clearAnimation();
                selectDietBtn.startAnimation(ani);
                Intent intent = new Intent(getApplicationContext(),SearchFood.class);
                startActivity(intent);
            }
        });

        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0x00000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        startAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MENU) {
            Toast.makeText(getApplicationContext(), "요청 코드: " + requestCode + " 결과 코드: " + resultCode, Toast.LENGTH_LONG).show();

            if (resultCode == RESULT_OK) {
                String msg = data.getExtras().getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startAnimation() {

        switcher.setVisibility(View.VISIBLE);
        thread = new ImageThread();
        thread.start();
    }

    private void stopAnimation() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
        switcher.setVisibility(View.INVISIBLE);
    }

    class ImageThread extends Thread {
        int duration = 1000;
        final int imageId[] = {R.drawable.close_eyes, R.drawable.open_mouth,R.drawable.open_eyes};
        int currentIndex = 0;

        @Override
        public void run() {
            running = true;

            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            switcher.setImageResource(imageId[currentIndex]);
                        }
                    });
                    currentIndex++;
                    if (currentIndex == imageId.length) {
                        currentIndex = 0;
                    }
                    try {
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public static DataBase getDBInstance() {
        return db;
    }
}
