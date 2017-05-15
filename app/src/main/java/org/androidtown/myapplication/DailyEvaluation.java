package org.androidtown.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.myapplication.calendar.Calendar_main;

import static android.R.attr.name;
import static org.androidtown.myapplication.R.id.naBar;
import static org.androidtown.myapplication.R.id.recommendBtn;

public class DailyEvaluation extends AppCompatActivity {
    Button btn;
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

        btn=(Button) findViewById(R.id.recommendBtn);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Calendar_main.class);
                startActivity(intent);
            }
        });

        ProgressBar naBar = (ProgressBar) findViewById(R.id.naBar);
        naBar.setIndeterminate(false);
        naBar.setProgress(70);
        naBar.setAnimation(growAnim);

        ProgressBar cholBar = (ProgressBar) findViewById(R.id.cholBar);
        cholBar.setIndeterminate(false);
          cholBar.setProgress(80);
        cholBar.setAnimation(growAnim);

        ProgressBar fatBar = (ProgressBar) findViewById(R.id.fatBar);
        fatBar.setIndeterminate(false);
        fatBar.setProgress(30);
        fatBar.setAnimation(growAnim);

        ProgressBar sugarBar = (ProgressBar) findViewById(R.id.sugarBar);
        sugarBar.setIndeterminate(false);
        sugarBar.setProgress(50);
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