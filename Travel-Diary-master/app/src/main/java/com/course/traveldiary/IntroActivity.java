package com.course.traveldiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class IntroActivity extends AppCompatActivity{
    Handler handler = new Handler();
    private static final int REQUEST_CODE_PERMISSION = 2;
    String GPSPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    Runnable r = new Runnable() {
        @Override
        public void run() {
            // 3초뒤에 다음화면으로 넘어가기 Handler 사용
            Intent intent = new Intent(getApplicationContext(), ChooseMenuActivity.class);
            startActivity(intent); // 다음화면으로 넘어가기
            finish(); // Activity 화면 제거
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        try {

            if ((ActivityCompat.checkSelfPermission(this, GPSPermission) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
            {
                ActivityCompat.requestPermissions(this, new String[]{GPSPermission, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

                dbHelper.insert_gpstest(1, "2019-12-5   오전 11:05:13", "37.5670859", "126.9932446", "blue");
                dbHelper.insert_diarytest(1, "2019-12-5   오전 11:05:13", "/sdcard/Pictures/KakaoTalk_20191215_221743014.png","챔프 커피", "챔프커피 라떼가 제일 맛있엉 !");

                dbHelper.insert_gpstest(2, "2019-12-5   오후 12:07:13", "37.5581719", "127.0045289", "blue");
                dbHelper.insert_diarytest(2, "2019-12-5   오후 12:07:13", "/sdcard/Pictures/IMG_8027.JPG","산타 돈부리", "동국대학교의 맛집!!");

                //dbHelper.insert_gpstest(3, "2019-12-5   오후 13:10:20", "37.5462527", "126.9690266", "blue");
                //dbHelper.insert_diarytest(3, "2019-12-5   오후 13:10:20", "/storage/emulated/0/DCIM/Camera/3.png","장충 체육관", "GS칼텍스 vs 흥국생명 경기 gs칼텍스 승!");

                dbHelper.insert_gpstest(3, "2019-12-5   오후 14:12:23", "37.5090394", "126.9612882", "yellow");

                //dbHelper.insert_gpstest(4, "2019-12-5   오후 15:12:51", "37.531544", "127.0316891", "blue");
                //dbHelper.insert_diarytest(4, "2019-12-5   오후 15:12:51", "/storage/emulated/0/DCIM/Camera/4.png","이태원 단골집", "이태원 단골집 파스타 별 다섯개 !");

                dbHelper.insert_gpstest(4, "2019-12-5   오후 16:13:11", "37.5041302", "127.0232743", "blue");
                dbHelper.insert_diarytest(4, "2019-12-5   오후 16:13:11", "/storage/emulated/0/DCIM/Camera/5.png","회기 이모네파전", "회기 이모네파전 무난한 맛");



                dbHelper.insert_gpstest(5, "2019-12-10   오전 09:11:46", "37.557527", "126.9222782", "blue");
                dbHelper.insert_diarytest(5, "2019-12-10   오전 09:11:46", "/storage/emulated/0/DCIM/Camera/6.png","뚝섬 유원지", "뚝섬 유원지 날씨 좋았음~!");

                dbHelper.insert_gpstest(6, "2019-12-10   오전 10:15:20", "37.6467805", "126.8926336", "blue");
                dbHelper.insert_diarytest(6, "2019-12-10   오전 10:15:20", "/storage/emulated/0/DCIM/Camera/7.png","강남 코다차야", "화연이랑 간 강남 코다차야 연어 소 17000");

                //dbHelper.insert_gpstest(8, "2019-12-10   오후 12:20:18", "37.5385844", "127.0490398", "blue");
                //dbHelper.insert_diarytest(8, "2019-12-10   오후 12:20:18", "/storage/emulated/0/DCIM/Camera/8.png","홍대 입구역", "나홀로 홍대 버스킹 구경");

                dbHelper.insert_gpstest(7, "2019-12-10   오후 13:22:28", "37.527394", "127.0383833", "blue");
                dbHelper.insert_diarytest(7, "2019-12-10   오후 13:22:28", "/storage/emulated/0/DCIM/Camera/9.png","고양 스타필드", "고양 스타필드 엄마랑~");

                dbHelper.insert_gpstest(8, "2019-12-10   오후 14:18:20", "37.5311241", "126.9635104", "blue");
                dbHelper.insert_diarytest(8, "2019-12-10   오후 14:18:20", "/storage/emulated/0/DCIM/Camera/10.png","맥도날드 구의점", "맥도날드 혼밥 대성공!");

                //dbHelper.insert_gpstest(11, "2019-12-10   오후 17:45:36", "37.5670859", "126.9932446", "blue");
                //dbHelper.insert_diarytest(11, "2019-12-10   오후 17:45:36", "/storage/emulated/0/DCIM/Camera/11.png","압구정 로데오", "동기들이랑 닭구이 먹으러 옴!");

                dbHelper.insert_gpstest(9, "2019-12-10   오후 18:47:45", "37.514082", "126.9394983", "yellow");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 다시 화면에 들어어왔을 때 예약
        handler.postDelayed(r, 3000); // 3초 뒤에 Runnable 객체 수행
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 화면을 벗어나면, handler 에 예약해놓은 작업을 취소
        handler.removeCallbacks(r); // 예약 취소
    }
}
