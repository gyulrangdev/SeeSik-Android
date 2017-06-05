package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

/**
 * Created by jaena on 2017-05-31.
 */

public class LoadingActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);

        try{
            SoundPool loadingSound = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
            int soundbeep = loadingSound.load(this.getApplication(),R.raw.loading3,1);
            Thread.sleep(600);
            loadingSound.play(soundbeep,1f,1f,0,0,1f);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
