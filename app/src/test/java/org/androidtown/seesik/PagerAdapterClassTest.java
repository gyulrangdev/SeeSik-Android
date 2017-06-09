package org.androidtown.seesik;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sohyeon on 2017-06-09.
 */
public class PagerAdapterClassTest {
    @Test
    public void getCount() throws Exception {
        Tutorial mToturial = new Tutorial();
        int cnt = 7;
        assertEquals("Toturial count",7,cnt);
    }

}