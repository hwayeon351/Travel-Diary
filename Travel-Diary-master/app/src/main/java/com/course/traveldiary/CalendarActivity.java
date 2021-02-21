package com.course.traveldiary;

import android.os.Bundle;
import android.widget.TextView;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    TextView gps_result;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        setTitle("Location Log");
        result = dbHelper.getResult_gps();
        gps_result = (TextView)findViewById(R.id.textView2);
        gps_result.setText(result);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}

