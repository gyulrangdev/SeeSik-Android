package org.androidtown.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.tsengvn.typekit.TypekitContextWrapper;


/**
 * Created by jaena on 2017-05-31.
 */

public class Tutorial extends AppCompatActivity {
    private ViewPager mPager;


    View.OnClickListener mCloseButtonClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

            PagerAdapterClass pagerAdapterClass = new PagerAdapterClass(getApplicationContext());
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(pagerAdapterClass);

        mCloseButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int infoFirst = 1;
                SharedPreferences a = getSharedPreferences("first",MODE_PRIVATE);
                SharedPreferences.Editor editor = a.edit();
                editor.putInt("first",infoFirst);
                editor.commit();
                finish();
            }
        };
    }
    public class PagerAdapterClass  extends PagerAdapter {

        private LayoutInflater mInflater;

        public PagerAdapterClass(Context context)
        {
            super();
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 7;//나중에 튜토리얼 화면 수 바꾸면.... 하하하핳
        }


        @Override
        public Object instantiateItem(View pager, int position)
        {
            View view = null;

            switch (position)
            {
                case 0:
                    view = mInflater.inflate(R.layout.tutorial1,null);
                    view.findViewById(R.id.tutorial1);
                    break;
                case 1:
                    view = mInflater.inflate(R.layout.tutorial2,null);
                    view.findViewById(R.id.tutorial2);
                    break;
                case 2:
                    view = mInflater.inflate(R.layout.tutorial3,null);
                    view.findViewById(R.id.tutorial3);
                    break;
                case 3:
                    view = mInflater.inflate(R.layout.tutorial4,null);
                    view.findViewById(R.id.tutorial4);
                    break;
                case 4:
                    view = mInflater.inflate(R.layout.tutorial5,null);
                    view.findViewById(R.id.tutorial5);
                    break;
                case 5:
                    view = mInflater.inflate(R.layout.tutorial6,null);
                    view.findViewById(R.id.tutorial6);
                    break;
                case 6:
                    view = mInflater.inflate(R.layout.tutorial7,null);
                    view.findViewById(R.id.tutorial7);
                    view.findViewById(R.id.close1).setOnClickListener(mCloseButtonClick);
                    break;
            }
            ((ViewPager)pager).addView(view, 0);
            return view;
        }

        @Override
        public boolean isViewFromObject(View pager, Object object) {
            return pager==object;
        }

        @Override

        public void destroyItem(View pager, int position, Object view) {

            ((ViewPager)pager).removeView((View)view);

        }


        @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override public Parcelable saveState() { return null; }

        @Override public void startUpdate(View arg0) {}

        @Override public void finishUpdate(View arg0) {}

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}


