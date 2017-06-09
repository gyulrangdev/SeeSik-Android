package org.androidtown.seesik;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import static java.sql.Types.NULL;
import static org.junit.Assert.*;

/**
 * Created by sohyeon on 2017-06-09.
 */
public class SearchFoodTest {

    SearchFood mSearchFood ;
    @Before
    public void setUp(){
        mSearchFood = new SearchFood();
    }
    //푸드 카테고리는 null이 될 수 없음.
    @Test
    public void AboutFoodType() throws Exception {
        assertTrue(mSearchFood.foodTypeList!=null);
    }

    //DB에서 저장된 모든 푸드 네임이 foodName에 들어가기 때문에
    //foodName은 null이 될 수 없음.
    @Test
    public void AboutFoodName() throws Exception{
        assertTrue(mSearchFood.foodName!=null);
    }

}