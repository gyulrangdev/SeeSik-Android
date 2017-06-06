package org.androidtown.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DailyEvaluation extends Fragment{
    LinearLayout mainLayout;
    ProgressDialog dialog;
    Resources res;
    Animation growAnim;
    TextView recommendText;
    static DataBase db;
    private Context context;

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

        showEvaluationText();

    }

    public void showEvaluationText()
    {
        String str = "나트륨 "+score[0]+"/25\n";
        str+="포화지방 " + score[1] + "/25\n";
        str+="콜레스테롤 " + score[2] + "/25\n";
        str+="당 " + score[3] + "/25 으로\n";
        str +="총 "+score[4]+" / 100 점이야.";
        recommendText.setText(str);
        typeWriter.TextAnimation(recommendText);

    }
    public void calEvaluationScore()
    {
        double naT=0,fatT=0,cholT=0,sugarT=0;
        int eachSocre = 25;
        int recommendAmount[] = new int[4];
        recommendAmount[0] = 2000;
        recommendAmount[1] = 15;
        recommendAmount[2] = 600;
        recommendAmount[3] = 50;

        int scoreStandard[] = new int[4];
        scoreStandard[0] = 200;
        scoreStandard[1] = 1;
        scoreStandard[2] = 60;
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
            score[3] = (int)(eachSocre - (cholT/scoreStandard[3]));
            if(score[3]<=0)
                score[3] =0;
        }
        score[4]=score[0]+score[1]+score[2]+score[3];
    }
}