package org.androidtown.seesik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class DailyEvaluation extends Fragment{

    ImageSwitcher s_ludarfEye, s_ludarfMouth;
    EyeThread eyeThread;
    MouthThread mouthThread;
    boolean running = false;
    Handler mHandler = new Handler();
    LinearLayout mainLayout;
    ProgressDialog dialog;
    Resources res;
    Animation growAnim;
    TextView recommendText;
    static DataBase db;
    private Context context;
    FrameLayout ludalf;
    TextView naText;
    TextView fatText;
    TextView cholText;
    TextView sugarText;

    TypeWriter typeWriter;

    double na, chol, fat, sugar;

    int score[]= new int[5];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_daily_evaluation,container,false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        res=getResources();
        context = getActivity().getApplicationContext();

        growAnim = AnimationUtils.loadAnimation(context,R.anim.grow);

        recommendText = (TextView) view.findViewById(R.id.recommendText);
        naText = (TextView) view.findViewById(R.id.naText);
        fatText = (TextView) view.findViewById(R.id.fatText);
        cholText = (TextView) view.findViewById(R.id.cholText);
        sugarText = (TextView) view.findViewById(R.id.sugarText);
        s_ludarfEye = (ImageSwitcher) view.findViewById(R.id.switcher_ludarf_eye);
        s_ludarfMouth = (ImageSwitcher) view.findViewById(R.id.switcher_ludarf_mouth);
        ludalf = (FrameLayout) view.findViewById(R.id.character_ludarf);
        typeWriter = new TypeWriter();

        db = MainActivity.getDBInstance();

        na = db.getNa();
        chol=db.getChol();
        fat=db.getFat();
        sugar=db.getSugar();

        naText.setText(na+" / 2000(mg)");
        fatText.setText(fat+" / 15(g)");
        cholText.setText(chol+" / 300(mg)");
        sugarText.setText(sugar+" / 50(g)");

        calEvaluationScore();

        ProgressBar naBar = (ProgressBar) view.findViewById(R.id.naBar);
        naBar.setIndeterminate(false);
        naBar.setProgress((int)na);
        naBar.setAnimation(growAnim);

        ProgressBar cholBar = (ProgressBar) view.findViewById(R.id.cholBar);
        cholBar.setIndeterminate(false);
        cholBar.setProgress((int)chol);
        cholBar.setAnimation(growAnim);

        ProgressBar fatBar = (ProgressBar) view.findViewById(R.id.fatBar);
        fatBar.setIndeterminate(false);
        fatBar.setProgress((int)fat);
        fatBar.setAnimation(growAnim);

        ProgressBar sugarBar = (ProgressBar) view.findViewById(R.id.sugarBar);
        sugarBar.setIndeterminate(false);
        sugarBar.setProgress((int)sugar);
        sugarBar.setAnimation(growAnim);
        soundPrepare();

        showEvaluationText();

        ludalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preference = context.getSharedPreferences("volume", MODE_PRIVATE);
                SharedPreferences mutePreference = context.getSharedPreferences("mute", MODE_PRIVATE);
                int mute = mutePreference.getInt("mute", 0);
                final float volume;
                if (mute != 1) {
                    volume = preference.getFloat("volume", 1f);
                    Log.d("volume", "" + volume);
                } else {
                    volume = 0f;
                }
                Random random = new Random();
                int ran = random.nextInt(2);
                sound.play(soundbeep[ran], volume, volume, 0, 0, 1);
            }
        });

        s_ludarfEye.setFactory(new ViewSwitcher.ViewFactory()
        {
            public View makeView() {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundColor(0x00000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        s_ludarfMouth.setFactory(new ViewSwitcher.ViewFactory()

        {
            public View makeView() {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundColor(0x00000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        startAnimation();
    }

    SharedPreferences preference;

    public void showEvaluationText()
    {
        String str = "나트륨 "+score[0]+"/25\n";
        str+="포화지방 " + score[1] + "/25\n";
        str+="콜레스테롤 " + score[2] + "/25\n";
        str+="당 " + score[3] + "/25 으로\n";
        str +="총 "+score[4]+" / 100 점이야.";
        recommendText.setText(str);
        preference = context.getSharedPreferences("effect", MODE_PRIVATE);
        int evEffect = preference.getInt("effect", 0);
        if(evEffect==0)
            typeWriter.TextAnimation(recommendText);

    }
    public void calEvaluationScore()
    {
        double naT=0,fatT=0,cholT=0,sugarT=0;
        int eachSocre = 25;
        int recommendAmount[] = new int[4];
        recommendAmount[0] = 2000;
        recommendAmount[1] = 15;
        recommendAmount[2] = 300;
        recommendAmount[3] = 50;

        int scoreStandard[] = new int[4];
        scoreStandard[0] = 200;
        scoreStandard[1] = 1;
        scoreStandard[2] = 30;
        scoreStandard[3] = 5;

        if(na<=recommendAmount[0])
            score[0]=eachSocre;
        else {
            naT = na - recommendAmount[0];
            score[0] = (int)(eachSocre - (naT/scoreStandard[0]));
            if(score[0]<=0)
                score[0] =0;
        }
        if(fat<=recommendAmount[1])
            score[1]=eachSocre;
        else {
            fatT = fat - recommendAmount[1];
            score[1] = (int)(eachSocre-(fatT/scoreStandard[1]));
            if(score[1]<=0)
                score[1] =0;
        }
        if(chol<=recommendAmount[2])
            score[2]=eachSocre;
        else {
            cholT = chol - recommendAmount[2];
            score[2] = (int)(eachSocre - (cholT/scoreStandard[2]));
            if(score[2]<=0)
                score[2] =0;
        }
        if(sugar<=recommendAmount[3])
            score[3]=eachSocre;
        else {
            sugarT = sugar - recommendAmount[3];
            score[3] = (int)(eachSocre - (sugarT/scoreStandard[3]));
            if(score[3]<=0)
                score[3] =0;
        }
        score[4]=score[0]+score[1]+score[2]+score[3];
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
        int duration = 500;
        final int imageId[] = {R.drawable.ludarf_open_eye, R.drawable.ludarf_close_eye};
        int currentIndex = 0;

        @Override
        public void run() {
            running = true;
            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            s_ludarfEye.setImageResource(imageId[currentIndex]);
                        }
                    });
                    currentIndex++;
                    if (currentIndex == imageId.length) {
                        currentIndex = 0;
                        try {
                            int sleepTime = getRandomTime(3000, 5000);
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }
                    try {
                        duration=getRandomTime(3000, 5000);
                        Thread.sleep(duration);
                    } catch (InterruptedException e) {
                    }
                }
            }   //while end

        }   //run end
    }

    class MouthThread extends Thread {

        int m_duration=500;
        final int image_m_Id[] = {R.drawable.ludarf_close_mouth, R.drawable.ludarf_open_mouth, R.drawable.ludarf_close_mouth, R.drawable.ludarf_open_mouth,R.drawable.ludarf_close_mouth, R.drawable.ludarf_open_mouth, R.drawable.ludarf_close_mouth, R.drawable.ludarf_open_mouth};
        int m_currentIndex = 0;

        @Override
        public void run() {
            running = true;
            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (m_currentIndex == image_m_Id.length) m_currentIndex = 0;
                            s_ludarfMouth.setImageResource(image_m_Id[m_currentIndex]);
                        }
                    });
                    m_currentIndex++;
                    if (m_currentIndex == image_m_Id.length) {
                        m_currentIndex = 0;
                        try {
                            int sleepTime = getRandomTime(10000, 12000);
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }
                    try {
                        Thread.sleep(m_duration);
                    } catch (InterruptedException e) {
                    }
                }
            }   //while end

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        calEvaluationScore();
        startAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int getRandomTime(int min, int max) {
        return min + (int) (Math.random() * (max - min));
    }
    private SoundPool sound;
    private int[] soundbeep;
    public void soundPrepare()
    {
        sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundbeep = new int[3];

        soundbeep[0] = sound.load(context, R.raw.leuheum, 1);
        soundbeep[1] = sound.load(context, R.raw.lheueum, 1);
        soundbeep[2] = sound.load(context, R.raw.lbbbb, 1);

    }
}