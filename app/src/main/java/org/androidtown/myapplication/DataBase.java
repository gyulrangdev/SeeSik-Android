package org.androidtown.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by jaena on 2017-05-09.
 */

public class DataBase extends AppCompatActivity{
    SQLiteDatabase db;
    final String name = "seeSik";

    public DataBase(Context context){
        try{
            db = context.openOrCreateDatabase(name, MODE_PRIVATE, null);//openOrCreateDatabase는 context가 필요해 오류가 났었음!
        }catch(Exception e){
        }
    }

    public void createTable()
    {//To create userInfo, intakeList in database
        db.execSQL("create table if not exists userInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, age integer, gender integer);");// Create userInfo table
        db.execSQL("create table if not exists intakeList(_id INTEGER PRIMARY KEY AUTOINCREMENT, date date, foodType int, foodIndex int, sugar double, chol double, na double, fat double, times int);");// Create intakeList table
    }

    public void insertUserInfo(int age, int gender)
    {
        db.execSQL("insert into userInfo(age, gender) values ('"+age+"','"+gender+"');");
    }

    public void insertIntakeList(int foodType, int foodIndex)
    {
        // 사용자가 음식을 먹었을 때, 추가되는 부분

        Date date = new Date();// 아직 오늘의 날짜 받는 방법을 모름. 추후 수정
        double sugar=0, chol=0,na=0,fat=0;
        int times =0;// 맨 처음이므로 0으로 설정

        // 아직 받는 부분은 생각하지 못함 음식 DB가 아직 정리가 .... 흡
        db.execSQL("insert into intakeList(date, foodType, foodIndex, sugar, chol, na, fat, times) values ('"+date+"','"+foodType+"','"+foodIndex+"','"+sugar+"','"+chol+"','"+na+"','"+fat+"','"+times+");");

    }

    public void deleteIntakeList(int foodType, int foodIndex)
    {
        //사용자가 음식을 지웠을 때, 지우는 부분
    }


}
