package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

public class Recommend extends Fragment {// please change this Recommend

    private Context context;

    DataBase db;

    TextView recommendTitle;
    TextView recommendContent;

    String[] naRecommendFoodList = {"배","바나나", "키위", "검은콩", "감자", "브로콜리", "양파", "고구마", "옥수수", "오이", "시금치", "호두"};
    int[] naRecommendAmountList = {170, 358, 290, 1240, 396, 316, 146, 337, 287, 147, 558, 441,};
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

        recommendTitle = (TextView) view.findViewById(R.id.recommendTitle);
        recommendContent = (TextView) view.findViewById(R.id.recommendContent);

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
            String naComment="<나트륨>\n";
            naComment += "현재 권장량 2000mg 중 "+excessAmount+" mg 초과했습니다.\n\n";
            naComment += naRecommendFoodList[r]+"을(를) 섭취하는 것을 추천합니다.\n";
            naComment += naRecommendFoodList[r]+"은(는) 100g 당 "+naRecommendAmountList[r]+" mg 만큼의 칼륨 함량을 가지고 있습니다.\n\n";
            naComment +="WTO 에서는 나트륨과 칼륨을 1:1 비율로 섭취하는 것을 권장합니다.\n";
            int rate=1;
            if(excessAmount>naRecommendAmountList[r])
            {
                rate= excessAmount/naRecommendAmountList[r];
            }
            naComment +="초과한 "+excessAmount+" mg 만큼의 칼륨을 섭취하려면\n";
            naComment +="약 "+rate*100+"g의 "+naRecommendFoodList[r]+"을(를) 섭취하는 것을 추천합니다.\n\n";
            comment += naComment;
        }
        if(chol>600)
        {
            int r = (int)(Math.random()*13);

            int excessAmount = chol - 600;
            String cholComment="<콜레스테롤>\n";
            cholComment += "현재 권장량 600mg 중 "+excessAmount+" mg 초과했습니다.\n\n";
            cholComment += cholRecommendFoodList[r]+"을(를) 섭취하는 것을 추천합니다.\n\n";

            comment += cholComment;
        }
        if(fat>50)
        {
            int fatCal = 9;

            int walkingCal = 4 ;// 70kg 5km/h 기준
            int runningCal = 10; // 70kg 10km/h 기준
            int cycleCal = 8;// 18km/h 기준 fatsecret참고

            int excessAmount = fat - 50;
            int excessCalorie = excessAmount * fatCal;
            String fatComment="<포화지방>\n";
            fatComment += "현재 권장량 50g 중 "+excessAmount+" g 초과했습니다.\n\n";
            fatComment += excessAmount+" g 을(를) 소모하기 위해서는\n\n";
            fatComment += excessCalorie+" kcal 만큼 운동해야 합니다.\n";
            fatComment += "걷기 (70kg 5km/h 기준): "+excessCalorie/walkingCal+" 분\n";
            fatComment += "달리기 (70kg 10km/h 기준) : "+excessCalorie/runningCal+" 분\n";
            fatComment += "자전거타기(70kg 18km/h 기준) : "+excessCalorie/cycleCal+" 분\n\n";

            comment += fatComment;

        }
        if(sugar>60)
        {
            int sugarCal = 4;

            int walkingCal = 4 ;// 70kg 5km/h 기준
            int runningCal = 10; // 70kg 10km/h 기준
            int cycleCal = 8;// 18km/h 기준 fatsecret참고

            int excessAmount = sugar - 60;
            int excessCalorie = excessAmount * sugarCal;
            String sugarComment="<당>\n";
            sugarComment += "현재 권장량 60g 중 "+excessAmount+" g 초과했습니다.\n\n";
            sugarComment += excessAmount+" g 을(를) 소모하기 위해서는\n\n";
            sugarComment += excessCalorie+" kcal 만큼 운동해야 합니다.\n\n";
            sugarComment += "걷기 (70kg 5km/h 기준): "+excessCalorie/walkingCal+" 분\n";
            sugarComment += "달리기 (70kg 10km/h 기준) : "+excessCalorie/runningCal+" 분\n";
            sugarComment += "자전거타기(70kg 18km/h 기준) : "+excessCalorie/cycleCal+" 분\n";

            comment += sugarComment;

        }
        recommendContent.setText(comment);
        comment="";
    }

}
