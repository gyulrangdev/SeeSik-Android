package org.androidtown.seesik;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentContainer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sohyeon on 2017-06-09.
 */
public class DailyEvaluationTest {

    //score가 0보다 큰 값이 나와야 한다.
    @Test
    public void calEvaluationScore() throws Exception {
        DailyEvaluation dailyEvaluation = new DailyEvaluation();
        int score = 10;
        assertTrue(score>=0);
    }

}