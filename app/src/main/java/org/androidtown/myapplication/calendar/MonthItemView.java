package org.androidtown.myapplication.calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by sohyeon on 2017-05-15.
 */

//일(day)에 표시하는 텍스트뷰 정의
public class MonthItemView extends AppCompatTextView {

    private MonthItem item;
    Typeface font;
    public MonthItemView(Context context) {
        super(context);
       font = Typeface.createFromAsset(getContext().getAssets(), "NanumBarunpenB.ttf");
    }

    public MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        font = Typeface.createFromAsset(getContext().getAssets(), "NanumBarunpenB.ttf");
    }

    public MonthItem getItem() {
        return item;
    }

    public void setItem(MonthItem item) {

        this.item = item;

        int day = item.getDay();
        if (day != 0) {
            setText(" "+String.valueOf(day));
            setTypeface(font);
        } else {
            setText(" ");
        }

    }

}
