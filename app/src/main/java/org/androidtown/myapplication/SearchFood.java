package org.androidtown.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class SearchFood extends AppCompatActivity {
    EditText searchBar;
    DataBase db;

    ListView foodType;// 대분류
    ListView foodsearchedList; //소분류
    ListView intakeList; // 섭취 리스트

    AutoCompleteTextView textView;

    String foodName[]={""};// AutoCompleteTextView 안에 들어갈 String list
    String [] foodTypeList = {"패스트푸드", "국", "튀김", "디저트","면","고기(족발, 보쌈 등)"};
    String []foodList= {};

    String [] fastFoodList= {"패스트푸드","피자","햄버거","핫도그","핫바","즉석 짜장,카레","삼각김밥","만두"};
    String [] soupList= {"국","된장국,찌개","김치찌개","국밥"};
    String [] noodleList= {"면","국수","라면","냉면","우동","자장면","짬뽕"};
    String [] dessertList = {"디저트","과자","빵","아이스크림","요거트","초콜릿","젤리","주스","커피"};
    String [] FriedList = {"튀김류","치킨","탕수육","돈까스","강정"};
    String [] meatList = {"고기류","족발","보쌈","햄,소시지"};

    boolean searchFoodFirst = true;// searchfood에 처음으로 들어갈 때 구분 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        db = new DataBase(SearchFood.this);

        AboutFoodName(SearchFood.this);
        AboutFoodType();
        AboutFoodList();
        AboutDailyIntakeList();
    }

    public void AboutFoodName(Context context)
    {
        textView = (AutoCompleteTextView) findViewById(R.id.searchBar);

        if(searchFoodFirst)
        {
            foodName=db.getAllFoodName();// 모든 string을 db에서 받아와 foodName에 넣음
            searchFoodFirst = false;
        }

        ArrayAdapter<String> SearchFoodAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,foodName);
        textView.setAdapter(SearchFoodAdapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = textView.getText().toString();
                db.insertItemInList("foodList",str);

                textView.setText("");
                // 자동완성에서 선택되면
                // intakeList에 자동추가,
                // text는 비워짐
            }
        });

    }

    public void AboutFoodType()
    {
        foodType = (ListView)findViewById(R.id.foodType);
        ArrayAdapter<String> foodTypeAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,foodTypeList);
        foodType.setAdapter(foodTypeAdapter);

    }

    public void AboutFoodList()
    {
        // foodList도 새롭게 바뀜(정확히 말하면 foodList를 구성하는 어레이가 바뀜)
        // foodList종류가 여러가지! 그거 엑셀에 나눠서 분류해서 따로 Listㅇㅇㅇㅇㅇ 해서 정의? 해두자
        foodsearchedList = (ListView)findViewById(R.id.foodList);//  소분류...
        ArrayAdapter<String> foodListAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,fastFoodList);
        foodsearchedList.setAdapter(foodListAdapter);
        //이것도 선택되면 intakeList에 자동추가
    }

    public void AboutDailyIntakeList()
    {//커스텀 리스트 뷰 사용!
        intakeList = (ListView) findViewById(R.id.intakeList);//이건 커스텀으로? 넣어야 할 듯....
    }

}