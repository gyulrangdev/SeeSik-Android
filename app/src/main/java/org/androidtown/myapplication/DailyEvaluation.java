package org.androidtown.myapplication;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.name;
import static org.androidtown.myapplication.R.id.naBar;

public class DailyEvaluation extends AppCompatActivity {
    LinearLayout mainLayout;
    ProgressDialog dialog;
    Resources res;
    Animation growAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_evaluation);
        res=getResources();
        growAnim = AnimationUtils.loadAnimation(this,R.anim.grow);

        ProgressBar naBar = (ProgressBar) findViewById(R.id.naBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);
        naBar.setAnimation(growAnim);

        ProgressBar cholBar = (ProgressBar) findViewById(R.id.cholBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);
        cholBar.setAnimation(growAnim);

        ProgressBar fatBar = (ProgressBar) findViewById(R.id.fatBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);
        fatBar.setAnimation(growAnim);

        ProgressBar sugarBar = (ProgressBar) findViewById(R.id.sugarBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);
        sugarBar.setAnimation(growAnim);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Toast.makeText(this,"그래프 애니메이션 활성화"+hasFocus,Toast.LENGTH_SHORT).show();
        if(hasFocus){
            growAnim.start();
        }
        else{
            growAnim.reset();
        }
    }
}