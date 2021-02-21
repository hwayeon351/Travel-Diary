package com.course.traveldiary;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_layout);
        setTitle("MY DIARY");

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        ArrayList<Diary> diary_list = new ArrayList<>();
        diary_list = dbHelper.getDiaryArr();

        MyAdapter myAdapter = new MyAdapter(diary_list);
        myRecyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}

