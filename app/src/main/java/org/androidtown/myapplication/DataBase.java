package org.androidtown.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.Telephony.Mms.Part.FILENAME;


public class DataBase extends AppCompatActivity {
    SQLiteDatabase foodDB;
    SQLiteDatabase userDB;
    final String userDBName = "seeSik";
    String TAG = "DATABASE";
    Context context;
    boolean first = true;

    public DataBase(Context c) {
        context = c;// 디비를 열고 닫을 때, context가 필요하므로
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);//openOrCreateDatabase는 context가 필요해 오류가 났었음!
        createTable();
    }

    public void createTable() {
        //To create userInfo, intakeList in database
        if (first) {
            userDB.execSQL("create table if not exists userInfo (age integer, gender integer);");// Create userInfo table
            userDB.execSQL("create table if not exists dailyList(date text,times integer,foodName text ,sugar real,na real,chol real,fat real );");// Create intakeList table
            userDB.execSQL("create table if not exists intakeList(date text, sugar real, na real, chol real, fat real, highestIngredient int);");
            File folder = new File("data/data/org.androidtown.myapplication/databases/");
            //에뮬 주소 data/user/0/org.androidtown.myapplication/databases/
            // 기기 주소 data/data/org.androidtown.myapplication/databases/
            if (!folder.exists()) folder.mkdir();
            File file = new File("data/data/org.androidtown.myapplication/databases/foodList.db");
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
            first = false;
        }
        userDB.close();
        foodDB = context.openOrCreateDatabase("foodList.db", MODE_PRIVATE, null);
        foodDB.close();
    }

    public String[] getAllFoodName() {
        foodDB = context.openOrCreateDatabase("foodList.db", MODE_PRIVATE, null);
        String SQL = "select foodName from foodList;";
        Cursor c1 = foodDB.rawQuery(SQL, null);
        int num = c1.getCount();

        String foodName[] = new String[num];
        for (int i = 0; i < num; i++) {
            c1.moveToNext();
            foodName[i] = c1.getString(0);
        }
        c1.close();
        foodDB.close();
        return foodName;
    }

    public void insertItemInList(String table, String str) {
        foodDB = context.openOrCreateDatabase("foodList.db", MODE_PRIVATE, null);
        String SQL = "select * from " +table + " where foodName = '" + str + "';";
        Cursor c1 = foodDB.rawQuery(SQL, null);

        int num = c1.getCount();
        double sugar = 0;
        double na = 0;
        double chol = 0;
        double fat = 0;
        // 어차피 name value로 같은 값을 찾기 때문에 따로 복사는 안함
        c1.moveToFirst();
        sugar = c1.getDouble(3);
        na = c1.getDouble(4);
        chol = c1.getDouble(5);
        fat = c1.getDouble(6);
        c1.close();
        foodDB.close();

        insertItemInDailyList(str, sugar, na, chol, fat);
        insertItemInIntakeList(sugar, na, chol, fat);
        calculateHighestIngredient();
        userDB.close();
    }

    private void insertItemInDailyList(String foodName, double sugar, double na, double chol, double fat) {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        String SQL = "select * from  dailyList where date = '" + strDate + "';";// 넣기 전, 같은 날짜의 값이 있는지 확인한다. // 후에 검색 초반으로 옮길 수 있으나 현재는 이렇게
        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();
        c1.moveToFirst();
        if (num == 0) {//  같은 날의 값이 없는 경우
            String SQL1 = "select * from dailyList;"; // 초기 사용시 리스트에는 아무 것도 없으므로 확인
            Cursor c2 = userDB.rawQuery(SQL1, null);
            int num1 = c2.getCount();
            c2.moveToFirst();
            if (num1 == 0) {//초기 사용이라면 리스트 안에 값을 넣음
                SQL1 = "insert into dailyList values(\"" + strDate + "\"," + 1 + ",\"" + foodName + "\"," + sugar + "," + na + "," + chol + "," + fat + ");";
                userDB.execSQL(SQL1);
            } else {//아니라면 새로운 날이므로 모든 리스트에 있는 값을 지운 후 새로운 값을 넣는다
                SQL1 = "delete from dailyList;";
                userDB.execSQL(SQL1);
                // dailyList안에 있는 모든 걸 지워야지
                // 그 다음 새롭게 값을 넣어야지
                SQL1 = "insert into dailyList values(\"" + strDate + "\"," + 1 + ",\"" + foodName + "\"," + sugar + "," + na + "," + chol + "," + fat + ");";
                userDB.execSQL(SQL1);
            }
        } else {
            //dailyList안에 값을 집어 넣어야지
            //그 전에 푸드 이름이 같은게 있나 확인
            String SQL1 = "select times from  dailyList where foodName = '" + foodName + "';"; // 음식이름이 같은게 있나 확인
            Cursor c2 = userDB.rawQuery(SQL1, null);
            num = c2.getCount();
            c2.moveToFirst();
            if (num == 1) {
                //있을 시, 횟수 늘려주기
                int time = c2.getInt(0);
                SQL1 = "update dailyList set times =" + (++time) + " where foodName = '" + foodName + "';";
                userDB.execSQL(SQL1);
            } else { //아무것도 없을 시 새로 값을 넣어줌
                SQL1 = "insert into dailyList values(\"" + strDate + "\"," + 1 + ",\"" + foodName + "\"," + sugar + "," + na + "," + chol + "," + fat + ");";
                userDB.execSQL(SQL1);
            }
        }
        //select로 값이 들어갔는지 확인
        String sql = "select * from dailyList where foodName = '" + foodName + "';";
        Cursor c3 = userDB.rawQuery(sql, null);
        int d = c3.getCount();
        c3.moveToFirst();
        Log.d("dailyList", "insert " + c3.getString(0) + " " + c3.getInt(1) + " " + c3.getString(2) + " " + c3.getDouble(3) + " " + c3.getDouble(4) + " " + c3.getDouble(5) + " " + c3.getDouble(6) + " ");
    }

    public void plusTimes(String foodName) {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        int originTimes, newTimes = 0;
        String SQL1 = "SELECT times FROM dailyList WHERE foodName='" + foodName + "'", SQL2;
        Cursor cur = userDB.rawQuery(SQL1, null);
        cur.moveToFirst();
        originTimes = cur.getInt(0);
        newTimes = originTimes + 1;
        Log.d("ASD", newTimes + "");
        SQL2 = "update dailyList set times =" + newTimes + " where foodName = '" + foodName + "';";

        userDB.execSQL(SQL2);
    }

    public void minusTimes(String foodName) {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        int originTimes, newTimes = 0;
        String SQL1 = "SELECT times FROM dailyList WHERE foodName='" + foodName + "'", SQL2;
        Cursor cur = userDB.rawQuery(SQL1, null);
        cur.moveToFirst();
        originTimes = cur.getInt(0);
        newTimes = originTimes - 1;
        SQL2 = "update dailyList set times =" + newTimes + " where foodName = '" + foodName + "';";

        userDB.execSQL(SQL2);
    }

    private void insertItemInIntakeList(double sugar, double na, double chol, double fat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        String SQL = "select * from  IntakeList where date = '" + strDate + "';"; // 같은 날의 value가 있는지 확인
        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();

        c1.moveToFirst();

        if (num == 0) {
            String SQL1 = "insert into IntakeList values('" + strDate + "'," + sugar + "," + na + "," + chol + "," + fat + "," + 0+");"; //아무것도 없다면 그냥 추가
            userDB.execSQL(SQL1);
        } else {
            c1.moveToFirst();
            double sugarTemp = c1.getDouble(1);
            double naTemp = c1.getDouble(2);
            double cholTemp = c1.getDouble(3);
            double fatTemp = c1.getDouble(4);

            sugarTemp = sugarTemp + sugar;
            naTemp = naTemp + na;
            cholTemp = cholTemp + chol;
            fatTemp = fatTemp + fat; // 값 업데이트

            String SQL1 = "update IntakeList set sugar =" + sugarTemp + ", na = " + naTemp + ", chol = " + cholTemp + ", fat= " + fatTemp + " where date = '" + strDate + "';";// 값 업데이트
            userDB.execSQL(SQL1);
        }
        c1.close();
        String sql = "select * from IntakeList where date='" + strDate + "';";
        Cursor c3 = userDB.rawQuery(sql, null);
        c3.moveToFirst();
        Log.d("dailyList", "insert " + c3.getString(0) + c3.getDouble(1) + " " + c3.getDouble(2) + " " + c3.getDouble(3) + " " + c3.getDouble(4) + " "+c3.getInt(5));
    }

    public int getItemTimes(String foodName) {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        String SQL = "select times from dailyList where foodName = '" + foodName + "';";
        Cursor c = userDB.rawQuery(SQL, null);
        c.moveToFirst();
        userDB.close();
        return c.getInt(0);
    }

    public void deleteItemInList(String foodName) {

        deleteItemInDailyList(foodName);
        calculateHighestIngredient();
    }

    private void deleteItemInDailyList(String foodName) {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        String SQL = "select times, sugar, na, chol, fat from  dailyList where foodName = '" + foodName + "';";
        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();
        int times = 0;

        double sugar = 0;
        double na = 0;
        double chol = 0;
        double fat = 0;
        c1.moveToFirst();

        times = c1.getInt(0);
        sugar = c1.getDouble(1);
        na = c1.getDouble(2);
        chol = c1.getDouble(3);
        fat = c1.getDouble(4);

        Log.d("dailyList", "delete " + c1.getString(0) + " " + times + " " + foodName + " " + sugar + " " + na + " " + chol + " " + fat + " ");
        String SQL1 = "delete from dailyList where foodName = '" + foodName + "';";
        userDB.execSQL(SQL1);
        c1.close();
        userDB.close();
        deleteItemInIntakeList(sugar, na, chol, fat);
    }

    private void deleteItemInIntakeList(double sugar, double na, double chol, double fat) {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);
        String SQL = "select * from  IntakeList where date = '" + strDate + "';";

        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();

        double sugarTemp = 0;
        double naTemp = 0;
        double cholTemp = 0;
        double fatTemp = 0;

        c1.moveToFirst();
        sugarTemp = c1.getDouble(1);
        naTemp = c1.getDouble(2);
        cholTemp = c1.getDouble(3);
        fatTemp = c1.getDouble(4);

        Log.d("intakeList", "delete before" + c1.getString(0) + " " + sugarTemp + " " + naTemp + " " + cholTemp + " " + fatTemp + "빼야 될 값 ->" + sugar + " " + na + " " + chol + " " + fat + " ");

        sugarTemp = sugarTemp - sugar;
        naTemp = naTemp - na;
        cholTemp = cholTemp - chol;
        fatTemp = fatTemp - fat; // 값 업데이트


        String SQL1 = "update IntakeList set sugar =" + sugarTemp + ", na = " + naTemp + ", chol = " + cholTemp + ", fat = " + fatTemp + " where date = '" + strDate + "';";// 값 업데이트
        userDB.execSQL(SQL1);

        String sql = "select * from IntakeList where date='" + strDate + "';";
        Cursor c3 = userDB.rawQuery(sql, null);
        c3.moveToFirst();
        Log.d("intakeList", "delete after" + c3.getString(0) + " " + c3.getString(1) + " " + c3.getString(2) + " " + c3.getString(3) + " " + c3.getString(4) + "");
        userDB.close();
    }

    public double getSugar() {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);
        String SQL = "select sugar from  IntakeList where date = '" + strDate + "';"; // 같은 날의 value가 있는지 확인

        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();
        double sugar = 0;
        if (num == 0) {
            num = 0;
        } else {
            c1.moveToFirst();
            sugar = c1.getDouble(0);
            c1.close();
            Log.d("getSugar", "" + sugar);
            userDB.close();
        }
        return sugar;
    }

    public double getNa() {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);
        String SQL = "select na from  IntakeList where date = '" + strDate + "';"; // 같은 날의 value가 있는지 확인

        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();
        double na;
        if (num == 0) {
            na = 0;
        } else {
            c1.moveToFirst();
            na = c1.getDouble(0);
            c1.close();
            Log.d("getNa", "" + na);
            userDB.close();
        }
        return na;
    }

    public double getChol() {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);
        String SQL = "select chol from  IntakeList where date = '" + strDate + "';"; // 같은 날의 value가 있는지 확인

        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();
        double chol;
        if (num == 0) {
            chol = 0;
        } else {
            c1.moveToFirst();
            chol = c1.getDouble(0);
            c1.close();
            Log.d("getChol", "" + chol);
            userDB.close();
        }
        return chol;
    }

    public double getFat() {
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);
        String SQL = "select fat from  IntakeList where date = '" + strDate + "';"; // 같은 날의 value가 있는지 확인

        Cursor c1 = userDB.rawQuery(SQL, null);
        int num = c1.getCount();
        double fat = 0;
        if (num == 0) {
            fat = 0;
        } else {
            c1.moveToFirst();
            fat = c1.getDouble(0);
            c1.close();
            Log.d("getFat", "" + fat);
            userDB.close();
        }
        return fat;
    }

    public void calculateHighestIngredient()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        double ingredient[] = new double[4]; //1 : Na 2: fat 3: chol 4: sugar
        ingredient[0] = getNa();
        ingredient[1] = getFat();
        ingredient[2] = getChol();
        ingredient[3] = getSugar();

        double recommendAmount[] = new double[4];
        recommendAmount[0] = 2000;
        recommendAmount[1] = 50;
        recommendAmount[2] = 600;
        recommendAmount[3] = 60;

        double highValue = ingredient[0]/ recommendAmount[0];
        int highIndex=0;
        double temp=0;
        for(int i=1;i<4;i++)
        {
            temp = ingredient[i]/recommendAmount[i];
            if(highValue< temp)
            {
                highValue = temp;
                highIndex = i;
            }
        }
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        userDB.execSQL("update intakeList set highestIngredient ="+(highIndex+1)+" where date = '"+strDate+"';");
    }

    public int getHighestIngredient() {
        //return 1 : Na 2: fat 3: chol 4: sugar
        userDB = context.openOrCreateDatabase(userDBName, MODE_PRIVATE, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);
        String SQL = "select highestIngredient from  IntakeList where date = '" + strDate + "';"; // 같은 날의 value가 있는지 확인

        Cursor c1 = userDB.rawQuery(SQL, null);

        c1. moveToFirst();
        int ingredient = c1.getInt(0);
        userDB.close();
        return ingredient;
    }
}

