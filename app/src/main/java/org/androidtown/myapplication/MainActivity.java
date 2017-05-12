package org.androidtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    Button evaBtn;
    Handler mHandler = new Handler();
    ImageThread thread;
    ImageSwitcher switcher;
    boolean running;

    DataBase db;

    db = new DataBase(MainActivity.this);
        db.createTable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        evaBtn = (Button) findViewById(R.id.evaluationBtn);
        switcher = (ImageSwitcher) findViewById(R.id.switcher);
        evaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DailyEvaluation.class);
                startActivity(intent);
            }
        });

        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0xFF000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });


    }

    public static class Monthlry extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_monthly);
        }
    }

//    private void startAnimation(){
//
//        switcher.setVisibility(View.VISIBLE);
//        thread = new ImageThread();
//        thread.start();
//    }
//    private void stopAnimation(){
//        running = false;
//        try{
//            thread.join();
//        }catch (InterruptedException e){}
//        switcher.setVisibility(View.INVISIBLE);
//    }

    class ImageThread extends Thread{
        int duration = 250;
        final int imageId[] = {R.drawable.closeeyes, R.drawable.openeyes};
        int currentIndex=0;

        @Override
        public void run() {
            running = true;

            while(running){
                synchronized (this){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            switcher.setImageResource(imageId[currentIndex]);
                        }
                    });

                    currentIndex++;
                    if(currentIndex==imageId.length){
                        currentIndex=0;
                    }

                    try{
                        Thread.sleep(duration);
                    }catch(InterruptedException e){}
                }
            }
        }
    }
}
