package org.androidtown.myapplication.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by sohyeon on 2017-05-15.
 */

//일(day)에 표시하는 텍스트뷰 정의
public class MonthItemView  extends AppCompatTextView {

    private MonthItem item;

    public MonthItemView(Context context) {
        super(context);

        init();
    }

    public MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setBackgroundColor(Color.WHITE);
    }


    public MonthItem getItem() {
        return item;
    }

    public void setItem(MonthItem item) {
        this.item = item;

        int day = item.getDay();
        if (day != 0) {
            setText(String.valueOf(day));
        } else {
            setText("");//비어있는 칸
        }
    }

    public void setSelectedItem(MonthItem item){
        this.item = item;
    }

}
