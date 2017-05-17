package org.androidtown.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import org.androidtown.myapplication.calendar.Calendar_main;

import static android.R.attr.name;
import static android.R.attr.onClick;
import static org.androidtown.myapplication.R.id.naBar;

public class DailyEvaluation extends Fragment{
    LinearLayout mainLayout;
    ProgressDialog dialog;
    Resources res;
    Animation growAnim;

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
        growAnim = AnimationUtils.loadAnimation(getContext(),R.anim.grow);

        ProgressBar naBar = (ProgressBar) view.findViewById(R.id.naBar);
        naBar.setIndeterminate(false);
        naBar.setProgress(70);
        naBar.setAnimation(growAnim);

        ProgressBar cholBar = (ProgressBar) view.findViewById(R.id.cholBar);
        cholBar.setIndeterminate(false);
        cholBar.setProgress(80);
        cholBar.setAnimation(growAnim);

        ProgressBar fatBar = (ProgressBar) view.findViewById(R.id.fatBar);
        fatBar.setIndeterminate(false);
        fatBar.setProgress(30);
        fatBar.setAnimation(growAnim);

        ProgressBar sugarBar = (ProgressBar) view.findViewById(R.id.sugarBar);
        sugarBar.setIndeterminate(false);
        sugarBar.setProgress(50);
        sugarBar.setAnimation(growAnim);

    }

}