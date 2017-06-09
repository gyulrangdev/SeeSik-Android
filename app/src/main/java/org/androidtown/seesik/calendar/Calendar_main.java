package org.androidtown.seesik.calendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidtown.seesik.DataBase;
import org.androidtown.seesik.MainActivity;
import org.androidtown.seesik.R;

import static android.content.Context.MODE_PRIVATE;


public class Calendar_main extends Fragment {

    GridView monthView;//월별 캘린더 뷰 객체
    MonthAdapter monthViewAdapter;//월별 캘린터 어댑터
    TextView naR, cholR, fatR, sugarR, normalR;//성분 별 Ratio
    TextView monthText, ratioTitle;//월 표시하는 텍스트뷰
    int curYear;//현재 연도
    int curMonth;//현재 월
    static DataBase db;
    DisplayMetrics displayMetrics;
    Typeface font;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_calendar, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        font = Typeface.createFromAsset(getContext().getAssets(), "NanumBarunpenB.ttf");
        context = getActivity().getApplicationContext();
        db = MainActivity.getDBInstance();
        displayMetrics = context.getResources().getDisplayMetrics();

        // 월별 캘린더 뷰 객체 참조
        monthView = (GridView) view.findViewById(R.id.monthView);
        monthViewAdapter = new MonthAdapter(context);
        monthView.setAdapter(monthViewAdapter);

        monthText = (TextView) view.findViewById(R.id.monthText);
        ratioTitle = (TextView) view.findViewById(R.id.ratioTitle);

        normalR = (TextView) view.findViewById(R.id.normalRatio);
        naR = (TextView) view.findViewById(R.id.naRatio);
        fatR = (TextView) view.findViewById(R.id.fatRatio);
        cholR = (TextView) view.findViewById(R.id.cholRatio);
        sugarR = (TextView) view.findViewById(R.id.sugarRatio);

        setMonthText();
        init();
        setRatioText();
        setRatio();

        // 이전 월로 넘어가는 이벤트 처리
        ImageButton monthPrevious = (ImageButton) view.findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
                setRatioText();
                init();
                setRatio();
            }
        });

        // 다음 월로 넘어가는 이벤트 처리
        ImageButton monthNext = (ImageButton) view.findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
                setRatioText();
                init();
                setRatio();
            }
        });

    }

    /**
     * 월 표시 텍스트 설정
     */
    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();
        monthText.setText(curYear + "년 " + curMonth + "월");
    }

    private void setRatioText() {
        ratioTitle.setTypeface(font);
        ratioTitle.setText(curMonth + "월의 분석");
    }

    private void init(){
        naR.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        naR.setText("");
        fatR.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        fatR.setText("");
        cholR.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        cholR.setText("");
        sugarR.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        sugarR.setText("");
    }
    private void setRatio() {
        Log.d("지금 몇 월", curMonth+"");
        int na = 0, chol = 0, fat = 0, sugar = 0, normal=0;
        int cnt = 0, tmpCnt=0;//tmpCnt는 전체 intake, cnt는 지금 포커스되는 달의 intake

        try {
            db.userDB = getActivity().openOrCreateDatabase("seeSik", MODE_PRIVATE, null);
            String SQL = "SELECT date, highestIngredient FROM intakeList ";
            Cursor c = db.userDB.rawQuery(SQL, null);
            c.moveToFirst();

            tmpCnt = c.getCount();

            if (tmpCnt != 0)
            {
                for (int i = 0; i < tmpCnt; i++) {

                    String tmpStr[] = c.getString(0).split("-");
                    int ptrMonth = Integer.parseInt(tmpStr[1]);

                    Log.d("디비에 있는 월", ptrMonth+"");
                    if (ptrMonth == curMonth)
                    {
                        switch (c.getInt(1)) {
                            case 1:
                                na += 1;
                                cnt++;
                                break;
                            case 2:
                                fat += 1;
                                cnt++;
                                break;
                            case 3:
                                chol += 1;
                                cnt++;
                                break;
                            case 4:
                                sugar += 1;
                                cnt++;
                                break;
                            case 5:
                                normal +=1;
                                cnt++;
                            default:
                                break;
                        }
                    }

                    c.moveToNext();
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.userDB.close();
        }

        if(normal >=0){
            if(normal==0){
                normalR.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
                normalR.setText("");
            }
            else{
                normalR.setLayoutParams(new LinearLayout.LayoutParams(Math.round(300 / cnt * normal * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)), LinearLayout.LayoutParams.MATCH_PARENT));
                normalR.setText(Double.parseDouble(String.format("%.1f", ((double) normal / (double) cnt) * 100)) + "%");
            }
        }
        if (na > 0) {
            naR.setLayoutParams(new LinearLayout.LayoutParams(Math.round(300 / cnt * na * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)), LinearLayout.LayoutParams.MATCH_PARENT));
            naR.setText(Double.parseDouble(String.format("%.1f", ((double) na / (double) cnt) * 100)) + "%");
        }
        if (fat > 0) {
            fatR.setLayoutParams(new LinearLayout.LayoutParams(Math.round(300 / cnt * fat * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)), LinearLayout.LayoutParams.MATCH_PARENT));
            fatR.setText(Double.parseDouble(String.format("%.1f", ((double) fat / (double) cnt) * 100)) + "%");
        }
        if (chol > 0) {
            cholR.setLayoutParams(new LinearLayout.LayoutParams(Math.round(300 / cnt * chol * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)), LinearLayout.LayoutParams.MATCH_PARENT));
            cholR.setText(Double.parseDouble(String.format("%.1f", ((double) chol / (double) cnt) * 100)) + "%");
        }
        if (sugar > 0) {
           sugarR.setLayoutParams(new LinearLayout.LayoutParams(Math.round(300 / cnt * sugar * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)), LinearLayout.LayoutParams.MATCH_PARENT));
           sugarR.setText(Double.parseDouble(String.format("%.1f", ((double) sugar / (double) cnt) * 100)) + "%");
        }

    }
}
