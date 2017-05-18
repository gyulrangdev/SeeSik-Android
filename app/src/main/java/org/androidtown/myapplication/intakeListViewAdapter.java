package org.androidtown.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static org.androidtown.myapplication.MainActivity.getDBInstance;

/**
 * Created by sohyeon on 2017-05-17.
 */

public class intakeListViewAdapter extends BaseAdapter {
    private ArrayList<intakeListViewItem> listViewItemList = new ArrayList<intakeListViewItem>();

    private static DataBase db;
    TextView intakeItemTxt;
    Button increBtn;
    Button decreBtn;
    Button deleteBtn;
    TextView intakeNumTxt;

    public intakeListViewAdapter() {
        db = MainActivity.getDBInstance();
    }
//TODO; 베베베베벱새삽이
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_intake_list_view_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        intakeItemTxt = (TextView) convertView.findViewById(R.id.intakeItemText);
        increBtn = (Button) convertView.findViewById(R.id.increaseBtn);
        decreBtn = (Button) convertView.findViewById(R.id.decreaseBtn);
        deleteBtn = (Button) convertView.findViewById(R.id.deleteBtn);
        intakeNumTxt = (TextView) convertView.findViewById(R.id.numText);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final intakeListViewItem intakelistViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
//            iconImageView.setImageDrawable(listViewItem.getIcon());
//            titleTextView.setText(listViewItem.getTitle());
//            descTextView.setText(listViewItem.getDesc());
        intakeItemTxt.setText(intakelistViewItem.getItemNameStr());
        intakeNumTxt.setText(intakelistViewItem.getItemNumStr()+"");

        final String _foodName = intakelistViewItem.getItemNameStr();
        increBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.plusTimes(intakelistViewItem.getItemNameStr());
                intakelistViewItem.setItemNumStr(db.getItemTimes(_foodName));
                intakeNumTxt.setText(intakelistViewItem.getItemNumStr()+"");
            }
        });

        decreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.minusTimes(intakelistViewItem.getItemNameStr());
                intakelistViewItem.setItemNumStr(db.getItemTimes(_foodName));
                intakeNumTxt.setText(intakelistViewItem.getItemNumStr()+"");
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // db.deleteItemInDailyIntakeList();

            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String name, int num) {
        intakeListViewItem item = new intakeListViewItem();

        item.setItemNameStr(name);
        item.setItemNumStr(num);

        listViewItemList.add(item);
    }
}

