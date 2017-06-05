package org.androidtown.myapplication;

import android.app.Application;

import com.tsengvn.typekit.Typekit;
/**
 * Created by WonHada.com on 2016-04-20.
 */

public class CustomStartApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumBarunpenR.ttf"))
                .addBold(Typekit.createFromAsset(this, "NanumBarunpenR.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "NanumBarunpenR.ttf"));// "fonts/폰트.ttf"
    }

}
