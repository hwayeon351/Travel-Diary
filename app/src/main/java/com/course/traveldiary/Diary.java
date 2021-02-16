package com.course.traveldiary;

public class Diary {
    int id;
    String memo;
    String place;
    String img_path;
    String date;

    public Diary(int id, String date, String place, String img_path, String memo){
        this.id = id;
        this.date = date;
        this.place = place;
        this.img_path = img_path;
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMemo() {
        return memo;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getPlace() {
        return place;
    }
}
