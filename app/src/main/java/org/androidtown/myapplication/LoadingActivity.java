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
            int r = (int)(Math.random()*2)+1;
            int soundbeep;
            if(r==1)
                soundbeep = loadingSound.load(this.getApplication(),R.raw.loading1,1);
            else
                soundbeep = loadingSound.load(this.getApplication(),R.raw.loading2,1);
            Thread.sleep(200);
            loadingSound.play(soundbeep,1f,1f,0,0,1f);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
