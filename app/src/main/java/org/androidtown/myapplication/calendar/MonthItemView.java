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
public class MonthItemView  extends View {

    private MonthItem item;
    private Canvas canvas = new Canvas();
    public MonthItemView(Context context) {
        super(context);

    }

    public MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    protected void onDraw(Canvas canvas){
        int day = item.getDay();
        if(day==0){

        }
        else {
            Paint pnt = new Paint();
            pnt.setColor(Color.BLUE);
            canvas.drawCircle(90, 60, 32, pnt);

            pnt.setColor(Color.BLACK);
            pnt.setAntiAlias(true);

            pnt.setTypeface(Typeface.create((String)null, Typeface.BOLD));
            pnt.setTextSize(35);

            String dayStr = day + "";
            canvas.drawText(dayStr, 10, 38, pnt);
        }
    }
    private void init() {
        setBackgroundColor(Color.WHITE);
    }


    public MonthItem getItem() {
        return item;
    }

    public void setItem(MonthItem item) {
        this.item = item;
    }


}
