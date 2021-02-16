package com.course.traveldiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseMenuActivity extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    Button map_btn, diary_btn, log_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_menu_layout);

        map_btn = findViewById(R.id.map_btn);
        diary_btn = findViewById(R.id.diary_btn);
        log_btn = findViewById(R.id.button);

        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.map_btn:
                        Intent map_intent = new Intent(ChooseMenuActivity.this, KakaoMapActivity.class);
                        startActivity(map_intent);
                        break;
                    case R.id.diary_btn:
                        Intent diary_intent = new Intent(ChooseMenuActivity.this,DiaryActivity.class);
                        startActivity(diary_intent);
                        break;
                    case R.id.button:
                        Intent calendar_intent = new Intent(ChooseMenuActivity.this,CalendarActivity.class);
                        startActivity(calendar_intent);
                }
            }
        };

        map_btn.setOnClickListener(onClickListener);
        diary_btn.setOnClickListener(onClickListener);
        log_btn.setOnClickListener(onClickListener);
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "뒤로 가기를 한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_LONG).show();
        }
    }
}
