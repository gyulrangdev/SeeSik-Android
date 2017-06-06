package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;

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
            SharedPreferences preference = getSharedPreferences("volume", MODE_PRIVATE);
            SharedPreferences mutePreference = getSharedPreferences("mute", MODE_PRIVATE);
            int mute = preference.getInt("mute", 0);
            float volume;
            if(mute!=1) {
                volume = preference.getFloat("volume", 1);
                Log.d("volume", "" + volume);
            }
            else
            {
                volume=0;
            }
            Thread.sleep(500);
            loadingSound.play(soundbeep,volume,volume,0,0,1);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
