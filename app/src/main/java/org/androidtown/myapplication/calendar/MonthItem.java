package org.androidtown.myapplication.calendar;

/**
 * Created by sohyeon on 2017-05-15.
 */

//'일(day)' 정보를 담기 위한 클래스
public class MonthItem {
        private int dayValue;

        public MonthItem() {

        }

        public MonthItem(int day) {
            dayValue = day;
        }

        public int getDay() {
            return dayValue;
        }

        public void setDay(int day) {
            this.dayValue = day;
        }

}
