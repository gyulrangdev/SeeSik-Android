package org.androidtown.seesik;

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
            int soundbeep;
            soundbeep = loadingSound.load(this.getApplication(),R.raw.loading2,1);
            SharedPreferences preference = getSharedPreferences("volume", MODE_PRIVATE);
            SharedPreferences mutePreference = getSharedPreferences("mute", MODE_PRIVATE);
            int mute = mutePreference.getInt("mute", 0);
            float volume;
            if(mute!=1) {
                volume = preference.getFloat("volume", 1f);
                Log.d("volume", "" + volume);
            }
            else
            {
                volume=0f;
            }
            Thread.sleep(1000);
            loadingSound.play(soundbeep,volume,volume,0,0,1);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
