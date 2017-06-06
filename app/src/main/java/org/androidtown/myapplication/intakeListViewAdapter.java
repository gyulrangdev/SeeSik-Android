package org.androidtown.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Created by sohyeon on 2017-05-17.
 */

public class intakeListViewAdapter extends BaseAdapter {
    private ArrayList<intakeListViewItem> listViewItemList = new ArrayList<intakeListViewItem>();

    private static DataBase db;
    TextView intakeItemTxt;
    ImageView increBtn;
    ImageView decreBtn;
    ImageView deleteBtn;

    public TextView intakeNumTxt;
    AlertDialog.Builder builder;

    public intakeListViewAdapter(Context c) {
        db = MainActivity.getDBInstance();
         builder= new AlertDialog.Builder(c);
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_intake_list_view_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        intakeItemTxt = (TextView) convertView.findViewById(R.id.intakeItemText);
        increBtn = (ImageView) convertView.findViewById(R.id.increaseBtn);
        decreBtn = (ImageView) convertView.findViewById(R.id.decreaseBtn);
        deleteBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);
        intakeNumTxt = (TextView) convertView.findViewById(R.id.numText);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final intakeListViewItem intakelistViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
//            iconImageView.setImageDrawable(listViewItem.getIcon());
//            titleTextView.setText(listViewItem.getTitle());
//            descTextView.setText(listViewItem.getDesc());
        intakeItemTxt.setText(intakelistViewItem.getItemNameStr());
        intakeNumTxt.setText(intakelistViewItem.getItemNum()+"");

        final String _foodName = intakelistViewItem.getItemNameStr();
        increBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.plusTimes(intakelistViewItem.getItemNameStr());
                intakelistViewItem.setItemNum(db.getItemTimes(_foodName));
                intakeNumTxt.setText(intakelistViewItem.getItemNum()+"");
                notifyDataSetChanged();
            }
        });

        decreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intakelistViewItem.getItemNum()==1){
                    builder.setTitle("삭제 확인 대화 상자")        // 제목 설정
                            .setMessage("삭제 하시겠습니까?")        // 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                // 확인 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton){
                                    db.deleteDailyList(intakelistViewItem.getItemNameStr());
                                    listViewItemList.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                                // 취소 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton){
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();
                }
                else
                {
                    db.minusTimes(intakelistViewItem.getItemNameStr());
                    intakelistViewItem.setItemNum(db.getItemTimes(_foodName));
                    notifyDataSetChanged();
                    intakeNumTxt.setText(intakelistViewItem.getItemNum() + "");
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("삭제 확인 대화 상자")        // 제목 설정
                        .setMessage("삭제 하시겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                db.deleteDailyList(intakelistViewItem.getItemNameStr());
                                listViewItemList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();
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
        item.setItemNum(num);
        Collections.reverse(listViewItemList);
        listViewItemList.add(item);
        Collections.reverse(listViewItemList);
    }
}

