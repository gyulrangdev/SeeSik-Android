package org.androidtown.myapplication;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    FrameLayout character, dayTimeBackground, nightTimeBackground;
    RelativeLayout flowerLeft, flowerRight;
    ImageView leftArm, leftEar, rightEar;
    ImageButton evaBtn, selectDietBtn, settingBtn;
    TextView interactTxt;
    Handler mHandler = new Handler();
    ArmThread armThread;
    EyeThread eyeThread;
    EarThread earThread;
    MouthThread mouthThread;
    ImageSwitcher switcher, switcher_m;
    boolean running;
    Animation armAnim, armDownAnim, lEarAnim, lEarDownAnim, rEarAnim, rEarDownAnim, flowerAnim;
    boolean isArmGoingDown = false;
    boolean isEarGoingDown = false;
    int hightestIndex = 0;
    static DataBase db;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
    SimpleDateFormat hourFormat = new SimpleDateFormat("HH",java.util.Locale.getDefault());
    Date date = new Date();
    String strDate = dateFormat.format(date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        evaBtn = (ImageButton) findViewById(R.id.evaluationBtn);
        selectDietBtn = (ImageButton) findViewById(R.id.selectDietBtn);
        settingBtn = (ImageButton) findViewById(R.id.settingBtn);
        switcher = (ImageSwitcher) findViewById(R.id.switcher);
        switcher_m = (ImageSwitcher) findViewById(R.id.switcher_mouth);
        character = (FrameLayout) findViewById(R.id.character);
        dayTimeBackground=(FrameLayout)findViewById(R.id.dayTimeBackground);
        nightTimeBackground=(FrameLayout)findViewById(R.id.nightTimeBackground);
        interactTxt = (TextView)findViewById(R.id.interactText);
        flowerLeft = (RelativeLayout) findViewById(R.id.flowerLayout_left);
        flowerRight = (RelativeLayout) findViewById(R.id.flowerLayout_right);
        leftArm = (ImageView) findViewById(R.id.leftArm);
        final Animation clickAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click_animation);
        final Animation clickUpAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click_up_animation);
        armAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arm_rotate);
        armDownAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.arm_down_rotate);
        lEarAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.l_ear_rotate);
        lEarDownAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.l_ear_down_rotate);
        rEarAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.r_ear_rotate);
        rEarDownAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.r_ear_down_rotate);
        flowerAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flower_rotate);
        leftEar = (ImageView) findViewById(R.id.leftEar);
        rightEar = (ImageView) findViewById(R.id.rightEar);
        SharedPreferences preference = getSharedPreferences("first", MODE_PRIVATE);
        int firstviewshow = preference.getInt("first", 0);
        if (firstviewshow != 1) {
            Intent intent = new Intent(MainActivity.this, Tutorial.class);
            startActivity(intent);
        }
        SharedPreferences DBpreference = getSharedPreferences("DBfirst", MODE_PRIVATE);
        int firstDB = DBpreference.getInt("DBfirst", 0);
        if(firstDB==0) {
            db = new DataBase(MainActivity.this,0);
            SharedPreferences a = getSharedPreferences("DBfirst",MODE_PRIVATE);
            SharedPreferences.Editor editor = a.edit();
            editor.putInt("DBfirst",1);
            editor.commit();
        }
        else
        {
            db = new DataBase(MainActivity.this,1);
        }
            hightestIndex = db.getHighestIngredient(strDate);
        if(hightestIndex==5)
            hightestIndex=0;
        setHighestIndexImage();
        setBackgroundImage();
        SoundPrepare();
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showSetting();
            }
        });

        flowerLeft.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==0){
                v.clearAnimation();
                setHighestIndexImage();
                flowerLeft.getChildAt(0).startAnimation(flowerAnim);}
            }
        });

        flowerLeft.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==1){
                v.clearAnimation();
                setHighestIndexImage();
                flowerLeft.getChildAt(1).startAnimation(flowerAnim);}
            }
        });

        flowerLeft.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==2){
                v.clearAnimation();
                setHighestIndexImage();
                flowerLeft.getChildAt(2).startAnimation(flowerAnim);
            }
            }
        });

        flowerLeft.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==3){
                v.clearAnimation();
                setHighestIndexImage();
                flowerLeft.getChildAt(3).startAnimation(flowerAnim);}
            }
        });

        flowerLeft.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==4){
                v.clearAnimation();
                setHighestIndexImage();
                flowerLeft.getChildAt(4).startAnimation(flowerAnim);}
            }
        });

        flowerRight.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==0){
                v.clearAnimation();
                setHighestIndexImage();
                flowerRight.getChildAt(hightestIndex).startAnimation(flowerAnim);}
            }
        });

        flowerRight.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==1){
                    v.clearAnimation();
                    setHighestIndexImage();
                    flowerRight.getChildAt(hightestIndex).startAnimation(flowerAnim);}
            }
        });

        flowerRight.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==2){
                    v.clearAnimation();
                    setHighestIndexImage();
                    flowerRight.getChildAt(hightestIndex).startAnimation(flowerAnim);}
            }
        });

        flowerRight.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==3){
                    v.clearAnimation();
                    setHighestIndexImage();
                    flowerRight.getChildAt(hightestIndex).startAnimation(flowerAnim);}
            }
        });

        flowerRight.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hightestIndex==4){
                    v.clearAnimation();
                    setHighestIndexImage();
                    flowerRight.getChildAt(hightestIndex).startAnimation(flowerAnim);}
            }
        });

        character.getChildAt(0).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final ImageView kkya = (ImageView) findViewById(R.id.kkya);
                v.clearAnimation();
                character.startAnimation(clickAni);
                character.startAnimation(clickUpAni);

                kkya.setVisibility(View.VISIBLE);

                SharedPreferences preference = getSharedPreferences("volume", MODE_PRIVATE);
                SharedPreferences mutePreference = getSharedPreferences("mute", MODE_PRIVATE);
                int mute = mutePreference.getInt("mute", 0);
                final float volume;
                if(mute!=1) {
                    volume = preference.getFloat("volume", 1f);
                    Log.d("volume", "" + volume);
                }
                else
                {
                    volume=0f;
                }

                Random random = new Random();
                int ran = random.nextInt(4);
                sound.play(soundbeep[ran],volume,volume,0,0,1);
                interactTxt.setText(db.getSaessabClickScript(0));

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                kkya.setVisibility(View.INVISIBLE);
                                interactTxt.setText(db.getSaessabScript());

                            }
                        });
                    }
                };

                Timer mTimer = new Timer();
                mTimer.schedule(task, 2000);

            }
        });


        evaBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                v.clearAnimation();
                evaBtn.startAnimation(clickAni);
                Intent intent = new Intent(getApplicationContext(), Evaluation.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        selectDietBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                v.clearAnimation();
                selectDietBtn.startAnimation(clickAni);
                Intent intent = new Intent(getApplicationContext(), SearchFood.class);
                startActivity(intent);
            }
        });

        switcher.setFactory(new ViewSwitcher.ViewFactory()

        {
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0x00000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        switcher_m.setFactory(new ViewSwitcher.ViewFactory()

        {
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0x00000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        interactTxt.setText(db.getSaessabScript());
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
        switcher_m.setVisibility(View.VISIBLE);
        eyeThread = new EyeThread();
        armThread = new ArmThread();
        earThread = new EarThread();
        mouthThread = new MouthThread();
        eyeThread.start();
        armThread.start();
        earThread.start();
        mouthThread.start();

    }


    private void stopAnimation() {
        running = false;
        interactTxt.setText(db.getSaessabScript());
        try {
            eyeThread.join();
            armThread.join();
            earThread.join();
        } catch (InterruptedException e) {
        }
        switcher.setVisibility(View.INVISIBLE);
        switcher_m.setVisibility(View.INVISIBLE);

    }

    class EarThread extends Thread {

        @Override
        public void run() {
            running = true;
            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isEarGoingDown) {
                                leftEar.startAnimation(lEarAnim);
                                rightEar.startAnimation(rEarAnim);
                                isEarGoingDown = false;
                            } else {
                                leftEar.startAnimation(lEarDownAnim);
                                rightEar.startAnimation(rEarDownAnim);
                                isEarGoingDown = true;
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
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
                            if (isArmGoingDown) {
                                leftArm.startAnimation(armDownAnim);
                                isArmGoingDown = false;
                            } else {
                                leftArm.startAnimation(armAnim);
                                isArmGoingDown = true;
                            }
                        }
                    });
                    try {
                        Thread.sleep(1300);
                    } catch (Exception e) {

                    }
                }

            }
        }
    }

    class MouthThread extends Thread {

        int m_duration;
        final int image_m_Id[] = {R.drawable.mouth_open, R.drawable.mouth_close};
        int m_currentIndex = 0;

        @Override
        public void run() {
            running = true;
            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            switcher_m.setImageResource(image_m_Id[m_currentIndex]);
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

    public void setHighestIndexImage() {
        for (int i = 0; i < flowerLeft.getChildCount(); i++) {
            flowerLeft.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < flowerRight.getChildCount(); i++) {
            flowerRight.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        flowerLeft.getChildAt(hightestIndex).setVisibility(View.VISIBLE);
        flowerRight.getChildAt(hightestIndex).setVisibility(View.VISIBLE);
    }

    public void setBackgroundImage() {
        String hour = hourFormat.format(date);
        int cHour = Integer.parseInt(hour);
        if(cHour>=18 || cHour <=6){
            nightTimeBackground.setVisibility(View.VISIBLE);
            dayTimeBackground.setVisibility(View.INVISIBLE);
        }
        else{
            nightTimeBackground.setVisibility(View.INVISIBLE);
            dayTimeBackground.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setBackgroundImage();
        Log.i("INDEX","onResume : "+hightestIndex);
        hightestIndex = db.getHighestIngredient(strDate);
        Log.i("INDEX","onResume : "+hightestIndex);
        if(hightestIndex==5)
            hightestIndex=0;
        Log.i("INDEX","onResume : "+hightestIndex);
        setHighestIndexImage();

        interactTxt.setText(db.getSaessabScript());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setBackgroundImage();
        hightestIndex = db.getHighestIngredient(strDate);
        if(hightestIndex==5)
            hightestIndex=0;
        setHighestIndexImage();
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

    private AlertDialog.Builder popDialog;
    private AlertDialog.Builder muteDialog;
    private AlertDialog.Builder volumeDialog;
    private AlertDialog.Builder evEffectDialog;
    private SeekBar seek;

    public void showSetting()
    {
        popDialog = new AlertDialog.Builder(this);
        muteDialog = new AlertDialog.Builder(this);
        volumeDialog = new AlertDialog.Builder(this);
        evEffectDialog = new AlertDialog.Builder(this);
        seek = new SeekBar(this);

        final CharSequence[] NormalItem = {"튜토리얼","음소거","볼륨 조절","일일평가 말풍선 효과","개발자 정보"};

        muteSetting();
        volumeSetting();
        effectSetting();

        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("설정");
        popDialog.setItems(NormalItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {
                    case 0:
                        startActivity(new Intent(MainActivity.this,Tutorial.class));
                        popDialog.show();//TODO: 여기 이게 먼저 보이고 액티비티가 시작 됨... 어떡하징... case 4,5도 같은 문제
                        break;
                    case 1://음소거
                        muteDialog.setTitle("음소거");
                        muteDialog.create();
                        muteDialog.show();
                        break;
                    case 2://볼륨 조절
                        SharedPreferences preference = getSharedPreferences("mute", MODE_PRIVATE);
                        int mute = preference.getInt("mute", 0);
                        if(mute==0){
                            volumeDialog.setTitle("볼륨 조절");
                            seek.setMax(10);
                            volumeDialog.setMessage("");
                            volumeDialog.setView(seek);
                            volumeDialog.create();
                            volumeDialog.show();}
                        else{
                            volumeDialog.setTitle("볼륨 조절");
                            volumeDialog.setMessage("음소거 중에는 볼륨 조절이 불가능 합니다.");
                            volumeDialog.create();
                            volumeDialog.show();
                        }
                        break;
                    case 3://일일평가 말풍선 효과
                        evEffectDialog.setTitle("일일평가 말풍선 효과");
                        evEffectDialog.create();
                        evEffectDialog.show();
                        break;
                    case 4://크레딧
                        startActivity(new Intent(MainActivity.this,DevInfo.class));
                        popDialog.show();
                        break;
                }
         }
        });

        popDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        popDialog.create();
        popDialog.show();


    }

    private void muteSetting()
    {
        final CharSequence[] MuteItem = {"음소거 X","음소거 O"};
        SharedPreferences preference = getSharedPreferences("mute", MODE_PRIVATE);
        int mute = preference.getInt("mute", 0);
        muteDialog.setSingleChoiceItems(MuteItem,mute, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0://음소거 X
                        SharedPreferences a = getSharedPreferences("mute", MODE_PRIVATE);
                        SharedPreferences.Editor editor = a.edit();
                        editor.putInt("mute", 0);
                        editor.commit();
                        break;
                    case 1://음소거 O
                        SharedPreferences b = getSharedPreferences("mute", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = b.edit();
                        editor2.putInt("mute", 1);
                        editor2.commit();
                        break;
                }
            }
        });
        muteDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                popDialog.show();
            }
        });
    }
    private void volumeSetting()
    {
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                SharedPreferences preference = getSharedPreferences("mute", MODE_PRIVATE);
                int mute = preference.getInt("mute", 0);
                if(mute==1)
                {
                    Toast.makeText(getApplicationContext(),"음소거 중에는 볼륨을 바꿀 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences a = getSharedPreferences("volume", MODE_PRIVATE);
                    SharedPreferences.Editor editor = a.edit();
                    editor.putFloat("volume", (float)(progress * 0.1));
                    Log.d("volume setting", "" + progress * 0.1);
                    editor.commit();
                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        volumeDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                popDialog.show();
            }
        });
    }
    private void effectSetting()
    {
        final CharSequence[] EffectItem = {"효과 O","효과 X"};
        SharedPreferences preference = getSharedPreferences("effect", MODE_PRIVATE);
        int evEffect = preference.getInt("effect", 0);
        evEffectDialog.setSingleChoiceItems(EffectItem,evEffect, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0://효과 O
                        SharedPreferences a = getSharedPreferences("effect", MODE_PRIVATE);
                        SharedPreferences.Editor editor = a.edit();
                        editor.putInt("effect", 0);
                        editor.commit();
                        break;
                    case 1://효과 X
                        SharedPreferences b = getSharedPreferences("effect", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = b.edit();
                        editor2.putInt("effect", 1);
                        editor2.commit();
                        break;
                }
            }
        });
        evEffectDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                popDialog.show();
            }
        });
    }

    private SoundPool sound;
    private int[] soundbeep;

    public void SoundPrepare()
    {
        sound = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundbeep = new int[4];

        soundbeep[0] = sound.load(getApplicationContext(),R.raw.ssaessap,1);
        soundbeep[1] = sound.load(getApplicationContext(),R.raw.ssae,1);
        soundbeep[2] = sound.load(getApplicationContext(),R.raw.ssaessap__,1);
        soundbeep[3] = sound.load(getApplicationContext(),R.raw.skkya,1);


    }
}
