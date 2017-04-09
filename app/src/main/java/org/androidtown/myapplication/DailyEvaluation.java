package org.androidtown.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class DailyEvaluation extends AppCompatActivity {
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_evaluation);

        ProgressBar naBar = (ProgressBar) findViewById(R.id.naBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);

        ProgressBar cholBar = (ProgressBar) findViewById(R.id.cholBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);

        ProgressBar fatBar = (ProgressBar) findViewById(R.id.fatBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);

        ProgressBar sugarBar = (ProgressBar) findViewById(R.id.sugarBar);
        naBar.setIndeterminate(false);
        naBar.setMax(100);
        naBar.setProgress(80);
    }
}
