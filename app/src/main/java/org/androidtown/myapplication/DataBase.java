package org.androidtown.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.Telephony.Mms.Part.FILENAME;


//foodType,foodIndex,foodName,sugar,na,chol,fat
public class DataBase extends AppCompatActivity {
    SQLiteDatabase foodDB;
    SQLiteDatabase userDB;
    final String userDBName = "seeSik";
    String TAG = "DATABASE";

    boolean first = true;

    public DataBase(Context context) {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);//openOrCreateDatabase는 context가 필요해 오류가 났었음!
        createTable(context);
    }

    public void createTable(Context context) {
        //To create userInfo, intakeList in database
        userDB.execSQL("create table if not exists userInfo (age integer, gender integer);");// Create userInfo table
        userDB.execSQL("create table if not exists dailyIntakeList(date text, foodType int, foodIndex int, times int);");// Create intakeList table
        userDB.execSQL("create table if not exists intakeList(date text, sugar real, chol real, na real, fat real);");
        if (first) {
            File folder = new File("data/user/0/org.androidtown.myapplication/databases/");
            //에뮬 주소 data/user/0/org.androidtown.myapplication/databases/
            // 기기 주소 data/data/org.androidtown.myapplication/databases/
            if (!folder.exists()) folder.mkdir();
            File file = new File("data/user/0/org.androidtown.myapplication/databases/foodList.db");
            //에뮬 주소data/user/0/org.androidtown.myapplication/databases/foodList.db
            //기기 주소 data/data/org.androidtown.myapplication/databases/foodList.db
            AssetManager assetManager = context.getAssets();
            try {

                file.createNewFile();

                InputStream is = assetManager.open("foodList.db");
                long filesize = is.available();

                byte[] tempdata = new byte[(int) filesize];

                is.read(tempdata);
                is.close();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(tempdata);
                fos.close();
                first = false;
            } catch (Exception e) {
                Toast.makeText(context, "오류가 났어....", Toast.LENGTH_LONG).show();
            }
            foodDB = context.openOrCreateDatabase("foodList.db", MODE_PRIVATE, null);
        }
    }

    public String[] getAllFoodName() {

        String SQL = "select foodName from foodList;";
        Cursor c1 = foodDB.rawQuery(SQL, null);
        int num = c1.getCount();

        String foodName[] = new String[num];
        for (int i = 0; i < num; i++) {
            c1.moveToNext();
            foodName[i] = c1.getString(0);
        }
        c1.close();

        return foodName;
    }

    public String insertItemInList(String table,String str) {
        String SQL = "select * from "+table+" where foodName = '" + str + "';";
        Cursor c1 = foodDB.rawQuery(SQL, null);

        int num = c1.getCount();
        int foodType = 0;
        int foodIndex = 0;
        double sugar = 0;
        double na = 0;
        double chol = 0;
        double fat = 0;

        for (int i = 0; i < num; i++) {
            c1.moveToNext();
            foodType = c1.getInt(0);
            foodIndex = c1.getInt(1);
            sugar = c1.getDouble(3);
            na = c1.getDouble(4);
            chol = c1.getDouble(5);
            fat = c1.getDouble(6);
        }
        c1.close();

        insertItemInDailyIntakeList(foodType, foodIndex);
        insertItemInIntakeList(sugar, na, chol, fat);
        // /""+num+" "+foodType+" "+foodIndex+" "+foodName+" "+sugar+" "+na+" "+chol+" "+fat;
    }

    public void insertItemInDailyIntakeList(int foodType, int foodIndex) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        String SQL = "select * from  dailyIntakeList where date = '" + strDate + "';";
        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();

        if (num == 0) {
            String SQL1 = "delete * from dailyIntakeList;";
            Cursor c2 = userDB.rawQuery(SQL1, null);
            // dailyIntakeList안에 있는 모든 걸 지워야지
            // 그 다음 새롭게 값을 넣어야지
            SQL1 = "insert into dailyIntakeList (date, foodType, foodIndex, times) values('"+strDate+"','"+foodType+"','"+0+"')";
            c2 = userDB.rawQuery(SQL1, null);
        } else {
            //dailyIntakeList안에 값을 집어 넣어야지
            //그 전에 푸드 index가 같은게 있나 확인
            String SQL1 = "select times from  dailyIntakeList where foodIndex = "+foodIndex+ ";";
            Cursor c2  = userDB.rawQuery(SQL1, null);
            num = c2.getCount();
            c2.moveToNext();
            int time = c2.getInt(0);
            if(num==1) {
                //있을 시, 횟수 늘려주기
                SQL1 = "update dailyIntakeList set times ="+(++time)+" where index = "+foodIndex+";";
                c2  = userDB.rawQuery(SQL1, null);
            }
            else {
                SQL1 = "insert into dailyIntakeList (date, foodType, foodIndex, times) values('"+strDate+"','"+foodType+"','"+0+"')";
                c2 = userDB.rawQuery(SQL1, null);
            }
        }
        //date text, foodType int, foodIndex int, times int
    }

    public void insertItemInIntakeList( double sugar, double na, double chol, double fat) {
        //date text, sugar real, chol real, na real, fat real
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        // 일단 디비에서 값을 가져와
        //오늘이랑 날짜 같은게 있나? 봐
        //없을 시, 새로 insert
        //있을 시, 값을 거기에 더해
    }

}
