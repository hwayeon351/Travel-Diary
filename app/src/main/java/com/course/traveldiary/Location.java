package com.course.traveldiary;

public class Location {
    String lat;
    String lng;
    String p_color;
    int id;

    public Location(int id, String lat, String lng, String p_color) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.p_color = p_color;
    }

    public int getId(){ return id; }
    public String getLat(){
        return lat;
    }
    public String getLng(){
        return lng;
    }
    public String getP_color() { return p_color; }
}
