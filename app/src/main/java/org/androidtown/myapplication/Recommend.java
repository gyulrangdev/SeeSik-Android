package org.androidtown.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Recommend extends Fragment {// please change this Recommend

    private Context context;

    DataBase db;

    String[] naRecommendFoodList = {"배","바나나", "키위", "검은콩", "감자", "브로콜리", "양파", "고구마", "옥수수", "오이", "시금치", "호두"};
    int[] naRecommendKaList = {170, 358, 290, 1860, 396, 316, 146, 337, 287, 147, 558, 441,};
    double [] naRecommendAmountList = { 0.25, 0.5, 1, 1, 1, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 10};
    String[] naUnitList={"개", "개", "개", "컵", "개", "개", "개", "개", "개", "개", "단", "알"}; //디비가 너무 귀찮았음... 흡 안귀찮을 때 디비로 바꿀게
    String[] cholRecommendFoodList = {"사과", "포도", "옥수수", "우유", "마늘", "부추", "양파", "표고버섯", "당근", "다시마", "아몬드", "굴", "감귤"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_recommend,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();

        //초과량
        TextView naExceed = (TextView)view.findViewById(R.id.naExceedTxt);
        TextView cholExceed = (TextView)view.findViewById(R.id.cholExceedTxt);
        TextView fatExceed = (TextView)view.findViewById(R.id.fatExceedTxt);
        TextView sugarExceed = (TextView)view.findViewById(R.id.sugarExceedTxt);


        //추천음식/활동
        TextView naRecommend = (TextView)view.findViewById(R.id.naRecommend);
        TextView fatRecommend = (TextView)view.findViewById(R.id.fatRecommend);
        TextView cholRecommend = (TextView)view.findViewById(R.id.cholRecommend);
        TextView sugarRecommend = (TextView)view.findViewById(R.id.sugarRecommend);

        //추천음식/활동 양(얼마나?)
        TextView naAmount = (TextView)view.findViewById(R.id.naRecommendAmount);
        TextView fatAmount = (TextView)view.findViewById(R.id.fatRecommendAmount);
        TextView cholAmount = (TextView)view.findViewById(R.id.cholRecommendAmount);
        TextView sugarAmount = (TextView)view.findViewById(R.id.sugarRecommendAmount);

        String comment = "";// 코멘트 달 것. 임시

        db = MainActivity.getDBInstance();

        int na = (int) db.getNa();
        int chol = (int) db.getChol();
        int fat = (int) db.getFat();
        int sugar = (int) db.getSugar();

        if(na>2000)
        {
            int r = (int)(Math.random()*12);
            int excessAmount = na - 2000;

            naExceed.setText(excessAmount+" mg");
            naRecommend.setText( naRecommendFoodList[r]+"\n"+naRecommendAmountList[r]+" "+naUnitList[r]+" 당 "+naRecommendKaList[r]+" mg");


            double rate=1;
            if(excessAmount>naRecommendKaList[r])
            {
                rate= excessAmount/naRecommendKaList[r];
            }
            naAmount.setText("약 "+rate*naRecommendAmountList[r]+" "+naUnitList[r]);
        }

        if(chol>300)
        {
            int r = (int)(Math.random()*13);

            int excessAmount = chol - 300;

            cholExceed.setText(excessAmount+" mg");
            cholRecommend.setText( cholRecommendFoodList[r]);

        }
        if(fat>15)
        {
            int fatCal = 9;

            int walkingCal = 4 ;// 70kg 5km/h 기준
            int runningCal = 10; // 70kg 10km/h 기준
            int cycleCal = 8;// 18km/h 기준 fatsecret참고

            int excessAmount = fat - 15;
            int excessCalorie = excessAmount * fatCal;

            fatExceed.setText(excessAmount+" g\n"+excessCalorie+" kcal");
            fatRecommend.setText("걷기\n달리기\n자전거타기");
            fatAmount.setText(excessCalorie/walkingCal+" 분\n");
            fatAmount.append(excessCalorie/runningCal+" 분\n");
            fatAmount.append(excessCalorie/cycleCal+" 분");
        }
        if(sugar>50)
        {
            int sugarCal = 4;

            int walkingCal = 4 ;// 70kg 5km/h 기준
            int runningCal = 10; // 70kg 10km/h 기준
            int cycleCal = 8;// 18km/h 기준 fatsecret참고

            int excessAmount = sugar - 50;
            int excessCalorie = excessAmount * sugarCal;

            sugarExceed.setText(excessAmount+" g\n"+excessCalorie+" kcal");
            sugarRecommend.setText("걷기\n달리기\n자전거타기");
            sugarAmount.setText(excessCalorie/walkingCal+" 분\n");
            sugarAmount.append(excessCalorie/runningCal+" 분\n");
            sugarAmount.append(excessCalorie/cycleCal+" 분");
        }
    }

}
