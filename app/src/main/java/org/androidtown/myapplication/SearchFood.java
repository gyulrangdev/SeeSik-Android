//package org.androidtown.myapplication;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.EditText;
//
//public class SearchFood extends AppCompatActivity {
//    EditText searchBar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_food);
//
//        searchBar = (EditText) findViewById(R.id.search);
//
//        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.search);
//
//        Intent intent = getIntent();
//        String temp = intent.getStringExtra("foodName");
//        String foodName[] = temp.split(",");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,foodName);
//        textView.setAdapter(adapter);
//
//    }
//}
