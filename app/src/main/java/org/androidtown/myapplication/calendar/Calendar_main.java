package org.androidtown.myapplication.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.androidtown.myapplication.R;


public class Calendar_main extends Fragment {

    GridView monthView;//월별 캘린더 뷰 객체
    MonthAdapter monthViewAdapter;//월별 캘린터 어댑터
    TextView monthText;//월 표시하는 텍스트뷰
    int curYear;//현재 연도
    int curMonth;//현재 월

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
        context = getActivity().getApplicationContext();
        // 월별 캘린더 뷰 객체 참조
        monthView = (GridView) view.findViewById(R.id.monthView);
        monthViewAdapter = new MonthAdapter(context);
        monthView.setAdapter(monthViewAdapter);

        monthText = (TextView) view.findViewById(R.id.monthText);
        setMonthText();

        // 이전 월로 넘어가는 이벤트 처리
        ImageButton monthPrevious = (ImageButton) view.findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        // 다음 월로 넘어가는 이벤트 처리
        ImageButton monthNext = (ImageButton) view.findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
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
}
