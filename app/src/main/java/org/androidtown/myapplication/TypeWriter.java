package org.androidtown.myapplication;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by jaena on 2017-06-07.
 */

public class TypeWriter {

    private Handler mHandler = new Handler();

    TextView t;
    int mIndex;
    CharSequence mText;
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            t.setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, 130);
            }
        }
    };

    public void TextAnimation(TextView textView)
    {
        t = textView;
        mText = t.getText();

        t.setText("");

        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder,130);
    }
}
