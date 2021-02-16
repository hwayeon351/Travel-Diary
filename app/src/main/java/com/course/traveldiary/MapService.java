package com.course.traveldiary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MapService extends Service implements LocationListener {
    private static final String TAG = "Location Changed";
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetGPSInfo = false;

    Location myLocation;
    String lat;
    String lng;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; //meter
    //private static final long MIN_TIME_UPDATES = 600000;
    private static final long MIN_TIME_UPDATES = 15000;
    protected LocationManager myLocationManager;
    public MapService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.i(TAG,"dd");
            myLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // do nothing
            } else {
                this.canGetGPSInfo = true;
                if (isNetworkEnabled) {
                    myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (myLocation != null) {
                        lat = Double.toString(myLocation.getLatitude());
                        lng = Double.toString(myLocation.getLongitude());
                    }
                }
                if (isGPSEnabled) {
                    if (myLocation == null) {

                        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (myLocationManager != null) {
                            myLocation =
                                    myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (myLocation != null) {
                                lat = Double.toString(myLocation.getLatitude());
                                lng = Double.toString(myLocation.getLongitude());
                                Log.i(TAG, "lat: " + lat + " lng: " + lng);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onLocationChanged(Location location) {
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        long now = System.currentTimeMillis();
        Date c_date = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd   a HH:mm:ss");

        String date  = simpleDateFormat.format(c_date);

        Log.d("test", "onLocationChanged, location:" + location);
        String lng = Double.toString(location.getLongitude()); //경도
        String lat = Double.toString(location.getLatitude());  //위도
        Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show();
        dbHelper.insert_gps(date ,lat, lng);
        Log.i("addGPS",dbHelper.getResult_gps());

    }


    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopUsingGPS();
        Log.i("Die","Service is destroyed");
        super.onDestroy();
    }
    public void stopUsingGPS() {
        if(myLocation != null) {
            Log.i("GPS","STOPPED");
            myLocationManager.removeUpdates(MapService.this);
        }
    }
}
