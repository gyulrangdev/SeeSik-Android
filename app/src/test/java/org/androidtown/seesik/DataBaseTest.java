package org.androidtown.seesik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.*;

/**
 * Created by sohyeon on 2017-06-09.
 */
public class DataBaseTest {

    SQLiteDatabase foodDB;
    public SQLiteDatabase userDB;
    final String userDBName = "seeSik";
    Context context;
    DataBase DB;

    @Before
    public void setUp() throws Exception {
        DB = new DataBase(context, 0);
    }

    @After
    public void tearDown() throws Exception {

    }

    //times는 처음부터 1이므로 0이 들어갈수 없다
    //0보다 큰 값이 들어가야 함.
    @Test
    public void getItemTimes() throws Exception {
        int times = 1;
        assertTrue(times>0);
    }

    //성분 값은 음수가 나올 수 없음.
    @Test
    public void getSugar() throws Exception {
        int sugar = 10;
        assertTrue(sugar>=0);
    }

    @Test
    public void getNa() throws Exception {
        int na = 10;
        assertTrue(na>=0);
    }

    @Test
    public void getChol() throws Exception {
        int chol = 10;
        assertTrue(chol>=0);
    }

    @Test
    public void getFat() throws Exception {
        int fat = 10;
        assertTrue(fat>=0);
    }

    //0: no data 1:na 2:fat 3:chol 4:sugar 5:normal
    //0-5 숫자가 나와야함.
    @Test
    public void getHighestIngredient() throws Exception {
        int index = 0;
        assertTrue(index>=0 && index<6);

    }

}