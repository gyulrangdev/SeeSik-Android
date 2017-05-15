package org.androidtown.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import static android.provider.Telephony.Mms.Part.FILENAME;

/**
 * Created by jaena on 2017-05-09.
 */
//foodType,foodIndex,foodName,sugar,na,chol,fat
public class DataBase extends AppCompatActivity{
    SQLiteDatabase foodDB;
    SQLiteDatabase userDB;
    final String userDBName = "seeSik";
    String TAG="DATABASE";

    boolean first= true;

    public DataBase(Context context) {//지금 현재는 main으로 되어있지만 후에 SearchFood? 에서 주로 씀! 아님 Database를 넘겨주던가!
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);//openOrCreateDatabase는 context가 필요해 오류가 났었음!
        createTable(context);
    }

    public void createTable(Context context) {
        //To create userInfo, intakeList in database
        userDB.execSQL("create table if not exists userInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, age integer, gender integer);");// Create userInfo table
        userDB.execSQL("create table if not exists intakeList(_id INTEGER PRIMARY KEY AUTOINCREMENT, date date, foodType int, foodIndex int, sugar real, chol real, na real, fat real, times int);");// Create intakeList table
        userDB.execSQL("create table if not exists dailyIntakeList(_id INTEGER PRIMARY KEY AUTOINCREMENT, date date, sugar real, chol real, na real, fat real);");
        if(first) {
            File folder = new File("data/user/0/org.androidtown.myapplication/databases/");
            //에뮬 주소 data/user/0/org.androidtown.myapplication/databases/
            // 기기 주소 data/data/org.androidtown.myapplication/databases/
            if(!folder.exists()) folder.mkdir();
            File file = new File("data/user/0/org.androidtown.myapplication/databases/foodList.db");
            //에뮬 주소data/user/0/org.androidtown.myapplication/databases/foodList.db
            //기기 주소 data/data/org.androidtown.myapplication/databases/foodList.db
            AssetManager assetManager = context.getAssets();
            try{

                file.createNewFile();

                InputStream is = assetManager.open("foodList.db");
                long filesize = is.available();

                byte[] tempdata = new byte[(int)filesize];

                is.read(tempdata);
                is.close();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(tempdata);
                fos.close();
                first = false;
            }catch(Exception e){
                Toast.makeText(context,"오류가 났어....",Toast.LENGTH_LONG).show();
            }
            foodDB = context.openOrCreateDatabase("foodList.db", MODE_PRIVATE, null);
        }
    }

    public void insertUserInfo(int age, int gender)
    {
        userDB.execSQL("insert into userInfo(age, gender) values ('"+age+"','"+gender+"');");
    }

    public void insertIntakeList(int foodType, int foodIndex)
    {
        // 사용자가 음식을 먹었을 때, 추가되는 부분

        Date date = new Date();// 아직 오늘의 날짜 받는 방법을 모름. 추후 수정
        double sugar=0, chol=0,na=0,fat=0;
        int times =0;// 맨 처음이므로 0으로 설정

        // 아직 받는 부분은 생각하지 못함 음식 DB가 아직 정리가 .... 흡
        userDB.execSQL("insert into intakeList(date, foodType, foodIndex, sugar, chol, na, fat, times) values ('"+date+"','"+foodType+"','"+foodIndex+"','"+sugar+"','"+chol+"','"+na+"','"+fat+"','"+times+");");

    }

    public void deleteIntakeList(int foodType, int foodIndex)
    {
        //사용자가 음식을 지웠을 때, 지우는 부분
    }


}
