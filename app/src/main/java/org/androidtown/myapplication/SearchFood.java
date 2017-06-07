package org.androidtown.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tsengvn.typekit.TypekitContextWrapper;

public class SearchFood extends AppCompatActivity {
    static DataBase db;

    ImageButton imageButton;
    ListView foodType;// 대분류
    ListView foodsearchedList; //소분류
    ListView intakeList; // 섭취 리스트
    intakeListViewAdapter intakeAdapter;//섭취리스트 어댑터

    AutoCompleteTextView textView;

    String foodName[] = {""};// AutoCompleteTextView 안에 들어갈 String list
    String[] foodTypeList = {"즐겨찾기", "패스트푸드", "국","면", "디저트", "튀김", "고기류","밥"};

    String[] selectedFoodList = {"즐겨찾기에 들어간 것이 없습니다."};

    boolean searchFoodFirst = true;// searchfood에 처음으로 들어갈 때 구분 변수

    int foodTypeNum = 0;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        builder = new AlertDialog.Builder(this);
        db = MainActivity.getDBInstance();

        intakeList = (ListView) findViewById(R.id.intakeList);

        if (db.checkDateChanged()) {
            db.resetDailyList();
        }

        loadList();
        loadFavoriteList();
        AboutFoodName(SearchFood.this);
        AboutFoodType();
        AboutFoodList();
        showAllThingDB();

        intakeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final intakeListViewItem item = (intakeListViewItem) intakeAdapter.getItem(position);
                builder.setTitle("즐겨찾기")        // 제목 설정
                        .setMessage(item.getItemNameStr() +" 을/를 즐겨찾기에 추가 하시겠습니까?")// 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                db.insertFavoriteList(item.getItemNameStr());
                                loadFavoriteList();
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

                return false;
            }
        });
    }
    public void loadList() {
        try {
            db.userDB = openOrCreateDatabase(db.userDBName, MODE_PRIVATE, null);
            String SQL = "SELECT foodName FROM dailyList";
            Cursor c = db.userDB.rawQuery(SQL, null);
            c.moveToFirst();
            int cnt = c.getCount();
            if (cnt != 0) {
                int[] times = db.getAlltimes();
                String[] foodName = db.getAllFoodName();
                intakeAdapter = new intakeListViewAdapter(this);
                intakeList.setAdapter(intakeAdapter);
                for (int i = 0; i < times.length; i++) {
                    intakeAdapter.addItem(foodName[i], times[i]);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.userDB.close();
        }
    }

    public void loadFavoriteList() {
        try {
            db.foodDB = openOrCreateDatabase("foodList.db", MODE_PRIVATE, null);
            String SQL = "SELECT foodName FROM simpleFoodList WHERE foodType=0;";
            Cursor c = db.foodDB.rawQuery(SQL, null);
            c.moveToFirst();
            int cnt = c.getCount();

            selectedFoodList = db.getRightList(0);
            ArrayAdapter<String> FoodListAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, selectedFoodList);
            foodsearchedList.setAdapter(FoodListAdapter);

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.foodDB.close();
        }
    }

    public void AboutFoodName(Context context) {
        textView = (AutoCompleteTextView) findViewById(R.id.searchBar);

        if (searchFoodFirst) {
            foodName = db.getFoodName();// 모든 string을 db에서 받아와 foodName에 넣음
            searchFoodFirst = false;
        }

        ArrayAdapter<String> SearchFoodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, foodName);
        textView.setAdapter(SearchFoodAdapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = textView.getText().toString();

                db.searchInDatabaseAndInsert("foodList", str);//오늘 먹은 음식 list에 삽입

                loadList();

                textView.setText("");

                //입력 후 키보드 감추기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            }
        });
    }

    public void AboutFoodType() {
        foodType = (ListView) findViewById(R.id.foodType);
        ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, foodTypeList);
        foodType.setAdapter(foodTypeAdapter);
        foodType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodTypeNum = position;
                changeRightList(position);
            }
        });
    }

    public void AboutFoodList() {
        // foodList도 새롭게 바뀜(정확히 말하면 foodList를 구성하는 어레이가 바뀜)
        // foodList종류가 여러가지! 그거 엑셀에 나눠서 분류해서 따로 Listㅇㅇㅇㅇㅇ 해서 정의? 해두자
        foodsearchedList = (ListView) findViewById(R.id.foodList);//  소분류...
        final ArrayAdapter<String> foodListAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, selectedFoodList);
        foodsearchedList.setAdapter(foodListAdapter);
        //이것도 선택되면 intakeList에 자동추가

        foodsearchedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = selectedFoodList[position];
                db.searchInDatabaseAndInsert("simpleFoodList", str);//오늘 먹은 음식 list에 삽입
                loadList();
            }

        });

        foodsearchedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (foodTypeNum == 0) {
                    final String str = selectedFoodList[position];
                    builder.setTitle("즐겨찾기 삭제 확인 대화 상자")        // 제목 설정
                            .setMessage("삭제 하시겠습니까?")        // 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                // 확인 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    db.deleteFavoriteList(str);
                                    loadFavoriteList();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                // 취소 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();
                    return true;
                }

                return true;
            }
        });
    }

    public void changeRightList(int position) {
        selectedFoodList = db.getRightList(position);
        ArrayAdapter<String> FoodListAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, selectedFoodList);
        foodsearchedList.setAdapter(FoodListAdapter);
    }

    public void showAllThingDB() {
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.logAll();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}