package org.androidtown.myapplication.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidtown.myapplication.R;

import static org.androidtown.myapplication.R.id.textView;

/**
 * Created by sohyeon on 2017-05-15.
 */

//일(day)에 표시하는 텍스트뷰 정의
public class MonthItemView extends AppCompatTextView {

    private MonthItem item;

    public MonthItemView(Context context) {
        super(context);

    }

    public MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MonthItem getItem() {
        return item;
    }

    public void setItem(MonthItem item) {
        this.item = item;

        int day = item.getDay();
        if (day != 0) {
            setText(" "+String.valueOf(day));
            setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            setText("");
        }

    }

}
