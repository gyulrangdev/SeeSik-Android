package org.androidtown.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    FrameLayout character;
    ImageButton nurseBtn;
    ImageView nurseView;
    ImageButton evaBtn;
    ImageButton selectDietBtn;
    Handler mHandler = new Handler();
    ArmThread armThread;
    EyeThread eyeThread;
    ImageSwitcher switcher;
    ImageView leftArm;
    boolean running;
    Animation armAnim;
    Animation armDownAnim;
    boolean isArmGoingDown = false;

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
        character = (FrameLayout) findViewById(R.id.character);
        leftArm = (ImageView) findViewById(R.id.leftArm);
        final Animation clickAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click_animation);
        final Animation clickUpAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click_up_animation);
        armAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arm_rotate);
        armDownAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arm_down_rotate);

        character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.clearAnimation();
                character.startAnimation(clickAni);
                character.startAnimation(clickUpAni);
            }
        });

        nurseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nurseView.getVisibility() == View.INVISIBLE) {
                    nurseView.setVisibility(View.VISIBLE);
                } else
                    nurseView.setVisibility(View.INVISIBLE);
            }
        });


        evaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.clearAnimation();
                evaBtn.startAnimation(clickAni);
                Intent intent = new Intent(getApplicationContext(), Evaluation.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        selectDietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.clearAnimation();
                selectDietBtn.startAnimation(clickAni);
                Intent intent = new Intent(getApplicationContext(), SearchFood.class);
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
        eyeThread = new EyeThread();
        armThread = new ArmThread();
        eyeThread.start();
        armThread.start();
    }

    private void stopAnimation() {
        running = false;
        try {
            eyeThread.join();
            armThread.join();
        } catch (InterruptedException e) {
        }
        switcher.setVisibility(View.INVISIBLE);
    }

    class EarThread extends Thread{

        @Override
        public void run() {
            running = true;
            while(running){
                synchronized (this){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });



                }


            }
        }
    }


    class ArmThread extends Thread {

        @Override
        public void run() {
            running = true;
            while (running) {
                synchronized (this) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isArmGoingDown){
                                leftArm.startAnimation(armDownAnim);
                                isArmGoingDown = false;
                            }else{
                                leftArm.startAnimation(armAnim);
                                isArmGoingDown = true;
                            }
                        }
                    });
                    try{
                        Thread.sleep(1300);
                    }catch (Exception e){

                    }
                }

            }
        }
    }


    class EyeThread extends Thread {
        int duration = 100;
        final int imageId[] = {R.drawable.eye_open, R.drawable.eye_close, R.drawable.eye_open, R.drawable.eye_close};
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

    public int getRandomTime(int min, int max) {
        return min + (int) (Math.random() * (max - min));
    }

    public static DataBase getDBInstance() {
        return db;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAnimation();
    }
}
