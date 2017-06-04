package org.androidtown.myapplication.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import org.androidtown.myapplication.DataBase;
import org.androidtown.myapplication.MainActivity;
import org.androidtown.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.String;

/**
 * Created by sohyeon on 2017-05-15.
 */

//어댑터 객체 정의
public class MonthAdapter extends BaseAdapter {

    DataBase db;

    // indredient color
    int[] indiredient = new int[] {
            Color.WHITE,
            Color.rgb(255, 213, 0),
            Color.rgb(255, 68, 68),
            Color.rgb(0, 221, 255),
            Color.rgb(170, 102, 204)
    };

    public static final String TAG = "MonthAdapter";
    Context mContext;

    public static int oddColor = Color.rgb(225, 225, 225);
    public static int headColor = Color.rgb(12, 32, 158);

    private MonthItem[] items;

    int mStartDay;
    int startDay;
    int curYear;
    int curMonth;

    int firstDay;
    int lastDay;

    Calendar mCalendar;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd", java.util.Locale.getDefault());
    Date date = new Date();
    String[] strDate = dateFormat.format(date).split(" ");
    int nowYear = Integer.parseInt(strDate[0]);
    int nowMonth = Integer.parseInt(strDate[1]);
    int nowDay = Integer.parseInt(strDate[2]);
    DisplayMetrics displayMetrics;

    public MonthAdapter(Context context) {
        super();
        mContext = context;
        displayMetrics = context.getResources().getDisplayMetrics();
        db = MainActivity.getDBInstance();
        init();
    }

    private void init() {
        items = new MonthItem[7 * 6];

        mCalendar = Calendar.getInstance();
        recalculate();
        resetDayNumbers();

    }

    public void recalculate() {

        // set to the first day of the month
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        // get week day
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        firstDay = getFirstDay(dayOfWeek);

        mStartDay = mCalendar.getFirstDayOfWeek();
        curYear = mCalendar.get(Calendar.YEAR);
        curMonth = mCalendar.get(Calendar.MONTH);
        lastDay = getMonthLastDay(curYear, curMonth);

        //Log.d(TAG, "nowYear : " + nowYear + ", nowMonth : " + nowMonth + ", nowDay : " + nowDay);

       // int diff = mStartDay - Calendar.SUNDAY - 1;
        startDay = getFirstDayOfWeek();
      //  Log.d(TAG, "mStartDay : " + mStartDay + ", startDay : " + startDay);

    }

    public void setPreviousMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        recalculate();

        resetDayNumbers();
    }

    public void setNextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        recalculate();

        resetDayNumbers();
    }

    public void resetDayNumbers() {
        for (int i = 0; i < 42; i++) {
            // calculate day number
            int dayNumber = (i+1) - firstDay;
            if (dayNumber < 1 || dayNumber > lastDay) {
                dayNumber = 0;
            }

            // save as a data item
            items[i] = new MonthItem(dayNumber);
        }
    }

    private int getFirstDay(int dayOfWeek) {
        int result = 0;
        if (dayOfWeek == Calendar.SUNDAY) {
            result = 0;
        } else if (dayOfWeek == Calendar.MONDAY) {
            result = 1;
        } else if (dayOfWeek == Calendar.TUESDAY) {
            result = 2;
        } else if (dayOfWeek == Calendar.WEDNESDAY) {
            result = 3;
        } else if (dayOfWeek == Calendar.THURSDAY) {
            result = 4;
        } else if (dayOfWeek == Calendar.FRIDAY) {
            result = 5;
        } else if (dayOfWeek == Calendar.SATURDAY) {
            result = 6;
        }

        return result;
    }


    public int getCurYear() {
        return curYear;
    }

    public int getCurMonth() {
        return curMonth+1;
    }

    public int getCount() {
        return 7 * 6;
    }

    public Object getItem(int position) {
        return items[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MonthItemView itemView;
        int tmpDay = items[position].getDay();
       // Log.d(TAG, "curYear : " + getCurYear() + ", curMonth : " + getCurMonth()+ " curDay(position): "+position+" curMonthLastDay: "+lastDay);
       //Log.d(TAG, "nowYear : " + nowYear + ", nowMonth : " + nowMonth + ", nowDay : " + nowDay);

        if (convertView == null) {
            itemView = new MonthItemView(mContext);
        } else {
            itemView = (MonthItemView) convertView;
        }

        // create a params
        GridView.LayoutParams params = new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                Math.round(43* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)));

        // set item data and properties
        itemView.setItem(items[position]);
        itemView.setLayoutParams(params);
        itemView.setPadding(2, 2, 2, 2);

        // set properties
        itemView.setGravity(Gravity.LEFT);
        itemView.setBackgroundColor(Color.WHITE);
        itemView.setTextColor(Color.BLACK);

         //set today's text color
        if (tmpDay == nowDay && getCurMonth()==nowMonth
                && getCurYear() ==nowYear) {
            itemView.setTextColor(Color.rgb(66, 134, 244));
        }

        if(getCurYear()<=nowYear && tmpDay>0 && tmpDay<=lastDay) {

            if(getCurYear()==nowYear && getCurMonth()==nowMonth && tmpDay<=nowDay)
                setExceedColor(itemView, tmpDay);
            else if(getCurYear()==nowYear && getCurMonth()>nowMonth){}
        }
        return itemView;
    }

    public void setExceedColor(View itemView,int day){
        String year = String.valueOf(getCurYear());
        String month = String.valueOf(getCurMonth());
        String curDay = String.valueOf(day);

        if(getCurMonth()<10)
            month="0"+String.valueOf(getCurMonth());

        if(day<10)
            curDay="0"+String.valueOf(day);


        String date =year+"-"+month+"-"+curDay;

        int highValue = db.getHighestIngredient(date);

        itemView.setBackgroundColor(indiredient[highValue]);

    }


    /**
     * Get first day of week as android.text.format.Time constant.
     * @return the first day of week in android.text.format.Time
     */
    public static int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
        }
    }


    /**
     * get day count for each month
     *
     * @param year
     * @param month
     * @return
     */
    private int getMonthLastDay(int year, int month){
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return (31);

            case 3:
            case 5:
            case 8:
            case 10:
                return (30);

            default:
                if(((year%4==0)&&(year%100!=0)) || (year%400==0) ) {
                    return (29);   // 2월 윤년계산
                } else {
                    return (28);
                }
        }
    }

}

