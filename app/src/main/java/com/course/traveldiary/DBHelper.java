package com.course.traveldiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DIARY (id INTEGER PRIMARY KEY, date TEXT NOT NULL, img_path TEXT, place TEXT, memo TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS GPS (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, lat TEXT NOT NULL, lng TEXT NOT NULL, p_color TEXT NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS FLAG (id INTEGER PRIMARY KEY, state INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void make_flag(int id, int state) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO FLAG VALUES( '" + id  + "', '"+ state + "');");
        db.close();
    }

    public void insert_gps(String date, String lat, String lng) {
        SQLiteDatabase db = getWritableDatabase();
        String p_color = "yellow";
        db.execSQL("INSERT INTO GPS(date, lat, lng, p_color) VALUES( '"+ date + "', '" + lat + "', '" + lng + "', '" + p_color + "');");
        db.close();
    }

    public void insert_gpstest(int id, String date, String lat, String lng, String p_color) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO GPS VALUES('" + id + "', '"+ date + "', '" + lat + "', '" + lng + "', '" + p_color + "');");
        db.close();
    }
    public void insert_diarytest(int id, String date, String img_path, String place, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DIARY VALUES('" + id + "', '" + date + "', '" + img_path + "', '" + place + "', '" + memo + "');");
        db.close();
    }

    public void insert_diary(int id, String date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DIARY(id, date) VALUES('" + id + "', '" + date + "');");
        db.close();
    }

    public void update_diary(int id, String img_path, String place, String memo){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DIARY SET img_path = '" + img_path + "' , place = '" + place + "' , memo = '" + memo + "' WHERE id = "+ id +";");
        Log.i("www", place+" ");
        db.close();
    }

    public void update_flag(int state){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE FLAG SET state = " + state + " WHERE id = 1;");
        db.close();
    }

    public void update_GPS(int id, String p_color){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE GPS SET p_color = '" + p_color + "' WHERE id = '" + id + "';");
        db.close();
    }

    public void delete_gps(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM GPS WHERE id ='" + id + "';");
        db.close();
    }

    public void delete_diary(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DIARY WHERE id='" + id + "';");
        db.close();
    }
    public String getResult_gps() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM GPS", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) //id
                    + "  |  "
                    + cursor.getString(1) //date
                    + "  |  "
                    + cursor.getString(2) //lat
                    + "  |  "
                    + cursor.getString(3) //lng
                    + "  |  "
                    + cursor.getString(4) //p_color
                    + "\n\n";
        }
        return result;
    }

    public String getResult_diary() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM DIARY", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) //id
                    + " | "
                    + cursor.getString(1) //date
                    + " | "
                    + cursor.getString(2) //img_path
                    + " | "
                    + cursor.getString(3) //place
                    + " | "
                    + cursor.getString(4) //memo
                    + "\n";
        }
        return result;
    }

    public int getFlagState(){
        SQLiteDatabase db = getReadableDatabase();
        int state = 0;
        Cursor cursor;
        cursor = db.rawQuery("SELECT state FROM FLAG WHERE id = 1", null);
        while (cursor.moveToNext()) {
            state = cursor.getInt(0);
        }
        Log.i("Gps","Table Updated");
        return state;
    }

    public String getGPSDate(int id){
        SQLiteDatabase db = getReadableDatabase();
        String date = "";
        Cursor cursor;
        //cursor = db.rawQuery("SELECT date FROM GPS WHERE id = " + id  +";", null);
        cursor = db.rawQuery("SELECT date FROM GPS WHERE id = "+ id +"", null);
        while (cursor.moveToNext()) {
            date = cursor.getString(0);
        }
        Log.i("aaaa",date + " ");
        return date;
    }

    public String getGPSpColor(int id){
        SQLiteDatabase db = getReadableDatabase();
        String p_color = "";
        Cursor cursor;
        //cursor = db.rawQuery("SELECT date FROM GPS WHERE id = " + id  +";", null);
        cursor = db.rawQuery("SELECT p_color FROM GPS WHERE id = "+ id +"", null);
        while (cursor.moveToNext()) {
            p_color = cursor.getString(0);
        }
        return p_color;
    }

    public int getGPSId(String lat, String lng){
        SQLiteDatabase db = getReadableDatabase();
        int id = 0;
        Cursor cursor;
        cursor = db.rawQuery("SELECT id FROM GPS WHERE lat = '" + lat + "' AND  lat = '" + lng + "'", null);
        while (cursor.moveToNext()) {
            //id = cursor.getInt(0);
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }
        Log.i("aaaa",id + " ");
        return id;
    }

    public String getDiaryImg(int id){
        SQLiteDatabase db = getReadableDatabase();
        String img_path = "";
        Cursor cursor;
        cursor = db.rawQuery("SELECT img_path FROM DIARY WHERE id = " + id + "", null);
        while (cursor.moveToNext()) {
            img_path = cursor.getString(0);
        }
        Log.i("iii",img_path + " ");
        return img_path;
    }

    public String getDiaryPlace(int id){
        SQLiteDatabase db = getReadableDatabase();
        String place = "";
        Cursor cursor;
        cursor = db.rawQuery("SELECT place FROM DIARY WHERE id = " + id + "", null);
        while (cursor.moveToNext()) {
            place = cursor.getString(0);
        }
        Log.i("iii", place+ " ");
        return place;
    }

    public String getDiaryMemo(int id){
        SQLiteDatabase db = getReadableDatabase();
        String memo = "";
        Cursor cursor;
        cursor = db.rawQuery("SELECT memo FROM DIARY WHERE id = " + id + "", null);
        while (cursor.moveToNext()) {
            memo = cursor.getString(0);
        }
        Log.i("iii",memo + " ");

        return memo;
    }

    public int checkFlag(){
        SQLiteDatabase db = getReadableDatabase();
        int cnt = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM FLAG", null);
        cnt = cursor.getCount();

        return cnt;
    }

    public int checkDiary(int id){
        SQLiteDatabase db = getReadableDatabase();
        int cnt = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM DIARY WHERE id = "+ id +"", null);
        cnt = cursor.getCount();

        return cnt;
    }

    public int getTodayCount(){
        SQLiteDatabase db = getReadableDatabase();
        int cnt = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM GPS WHERE date LIKE '%2019-12-14%'", null);
        cnt = cursor.getCount();

        return cnt;
    }

    public ArrayList<Location> getLocationArr(String date) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT id, lat, lng, p_color FROM GPS WHERE date LIKE '%"+ date +"%'", null);

        ArrayList<Location> location_list = new ArrayList<Location>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String lat = cursor.getString(1);
            String lng = cursor.getString(2);
            String p_color = cursor.getString(3);

            location_list.add(new Location(id, lat,lng, p_color));
            Log.i("ddd",lat +" "+ lng + " " + p_color);
            location_list.add(new Location(id, lat, lng, p_color));
        }
        return location_list;
    }

    public ArrayList<Diary> getDiaryArr() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM DIARY", null);

        ArrayList<Diary> diary_list = new ArrayList<Diary>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            String img_path = cursor.getString(2);
            String place = cursor.getString(3);
            String memo = cursor.getString(4);

            Log.i("abcd",id +" "+ date + " " + img_path + " " + place + " " + memo);
            diary_list.add(new Diary(id, date,  place, img_path, memo));
        }
        return diary_list;
    }

    public ArrayList<String> getCalendarArr(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Date FROM GPS", null);

        ArrayList<String> calendar_list = new ArrayList<>();
        while (cursor.moveToNext()){
            String date = cursor.getString(0);
            String front_date = date.split(" ")[0];
            boolean exist_date = false;
            for (int i=0; i<calendar_list.size();i++){
                String val = calendar_list.get(i);
                if(val.equals(front_date)){
                    exist_date = true;
                }
            }
            if(exist_date==false){
                calendar_list.add(front_date);
            }
        }
        return calendar_list;
    }

}
