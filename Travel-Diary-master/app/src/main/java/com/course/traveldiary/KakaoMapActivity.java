package com.course.traveldiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;

public class KakaoMapActivity extends AppCompatActivity implements MapView.POIItemEventListener {
    Button service_btn;
    int ck_flag;
    MapView mapView;
    ViewGroup mapViewContainer;
    private SimpleCalendarDialogFragment simpleCalendarDialogFragment;
    private Button date_pic_bt;
    private Calendar now;
    private String selectedDateStr = "";
    private int year;
    private int month;
    private int day;
    private String today = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_map);

        now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DAY_OF_MONTH);
        selectedDateStr = String.format("%d-%d-%d", year, month+1, day);

        date_pic_bt = (Button)findViewById(R.id.date_pic_bt);
        date_pic_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleCalendarDialogFragment.show(getSupportFragmentManager(), "Simple-Calendar");
            }
        });
        date_pic_bt.setText(selectedDateStr);

        long now = System.currentTimeMillis();
        Date c_date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today  = simpleDateFormat.format(c_date);

        if(today != selectedDateStr){
            //service_btn.setVisibility(View.INVISIBLE);
        }


        setTitle("MAP");
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setPOIItemEventListener(this);
        simpleCalendarDialogFragment = new SimpleCalendarDialogFragment();
        simpleCalendarDialogFragment.setOnDataChangedMyListener(new DateChangedListener() {
            @Override
            public void DateChanged(String date) {
                selectedDateStr = date;
                date_pic_bt.setText(selectedDateStr);
                initmapInfo();
            }
        });
/*
        int cnt = dbHelper.getTodayCount();
        ArrayList<Location> today_location = dbHelper.getLocationArr("2019-12-14");

        for(int i = 0; i < today_location.size(); i++){
            Double lat = Double.parseDouble(today_location.get(i).getLat());
            Double lng = Double.parseDouble(today_location.get(i).getLng());
            MapPOIItem marker = new MapPOIItem();
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
            marker.setItemName("test");
            mapView.addPOIItem(marker);
        }
*/

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.558608, 126.999138), 5, true);

        service_btn = (Button) findViewById(R.id.service_btn);
        ck_flag = dbHelper.checkFlag();
        if(ck_flag == 0){
            service_btn.setText("START TRAVELING");
        }else if(ck_flag == 1){
            int flag_state = dbHelper.getFlagState();
            if(flag_state == 0){
                service_btn.setText("START TRAVELING");
            }
            else if(flag_state == 1){
                Toast.makeText(KakaoMapActivity.this, "KEEP TRAVELING", Toast.LENGTH_SHORT).show();
                service_btn.setText("FINISH TRAVELING");
            }
        }

        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KakaoMapActivity.this, "flag" + ck_flag, Toast.LENGTH_SHORT).show();

                if(ck_flag == 0){
                    startService(new Intent(getBaseContext(),MapService.class));
                    Toast.makeText(KakaoMapActivity.this, "START TRAVELING", Toast.LENGTH_SHORT).show();
                    service_btn.setText("FINISH TRAVELING");
                    dbHelper.make_flag(1, 1);
                    Log.i("Flag created","now");
                }else if(ck_flag == 1){
                    int flag_state = dbHelper.getFlagState();
                    Log.i("Flag state", Integer.toString(flag_state));
                    if(flag_state == 0){
                        startService(new Intent(getBaseContext(),MapService.class));
                        Toast.makeText(KakaoMapActivity.this, "START TRAVELING", Toast.LENGTH_SHORT).show();
                        service_btn.setText("FINISH TRAVELING");
                        dbHelper.update_flag(1);
                    }else if(flag_state == 1){
                        stopService(new Intent(getBaseContext(),MapService.class));
                        Toast.makeText(KakaoMapActivity.this, "STOP TRAVELING", Toast.LENGTH_SHORT).show();
                        service_btn.setText("Start Traveling");
                        dbHelper.update_flag(0);
                    }
                }
            }
        });

    }



    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onResume(){
        super.onResume();
        initmapInfo();
        Log.i("Test","Resume");
    }

    public void initmapInfo(){
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        mapView.removeAllPOIItems();

        int cnt = dbHelper.getTodayCount();
        ArrayList<Location> today_location = dbHelper.getLocationArr(selectedDateStr);

        for(int i = 0; i < today_location.size(); i++){
            Double lat = Double.parseDouble(today_location.get(i).getLat());
            Double lng = Double.parseDouble(today_location.get(i).getLng());
            int id = today_location.get(i).getId();
            String p_color = today_location.get(i).getP_color();
            Log.i("marker",p_color + " ");

            if(p_color.contains("red")){
                MapPOIItem marker = new MapPOIItem();
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
                marker.setItemName("");
                marker.setTag(id);
                mapView.addPOIItem(marker);

            }
            else if(p_color.contains("yellow")){
                Log.i("marker",p_color + " marker ");
                MapPOIItem marker = new MapPOIItem();
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                marker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
                marker.setTag(id);
                marker.setItemName("Add Diary");
                mapView.addPOIItem(marker);
            }
            else if(p_color.contains("blue")){
                MapPOIItem marker = new MapPOIItem();
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                marker.setTag(id);
                marker.setItemName("Show Diary");
                mapView.addPOIItem(marker);
            }


            //marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            //marker.setCustomImageAutoscale(false);
            //marker.setCustomImageResourceId(R.drawable.yellow_pin);

        }
    }


    @Override
    public void onPOIItemSelected(final MapView mapView, final MapPOIItem mapPOIItem) {
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        final CharSequence[] items = {"기록하기","버리기"};
        AlertDialog.Builder diary_menu = new AlertDialog.Builder(this);
        diary_menu.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MapPoint point = mapPOIItem.getMapPoint();

                String p_lat = Double.toString(point.getMapPointGeoCoord().latitude);
                String p_lng = Double.toString(point.getMapPointGeoCoord().longitude);
                Log.i("markerLocation", p_lat + " " + p_lng);

                int p_id = mapPOIItem.getTag();
                String date = dbHelper.getGPSDate(p_id);

                Log.i("adds",p_id + " " + date);

                if(i == 0){
                    if(dbHelper.checkDiary(p_id) == 0){
                        dbHelper.insert_diary(p_id, date);
                    }
                    Intent intent = new Intent(KakaoMapActivity.this,EditDiaryActivity.class);
                    intent.putExtra("id", p_id);
                    intent.putExtra("date", date);
                    startActivity(intent);
                }
                else if(i == 1){
                    dbHelper.delete_gps(p_id);
                    dbHelper.delete_diary(p_id);
                    Toast.makeText(KakaoMapActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    mapView.removePOIItem(mapPOIItem);
                }
            }
        })
                .setCancelable(true)
                .show();
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }



    public static class SimpleCalendarDialogFragment extends AppCompatDialogFragment
            implements OnDateSelectedListener {
        String selected_date_str = "";
        private DateChangedListener listener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final DBHelper dbHelper = new DBHelper(getActivity(), "TravelDiary.db", null, 1);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            //inflate custom layout and get views
            //pass null as parent view because will be in dialog layout
            View view = inflater.inflate(R.layout.dialog_basic, null);


            MaterialCalendarView widget = view.findViewById(R.id.calendarView);

            ArrayList<String> dateList = new ArrayList<String>();
            dateList = dbHelper.getCalendarArr();
            ArrayList<DayViewDecorator> decoList = new ArrayList<DayViewDecorator>();
            for(int i=0;i<dateList.size();i++){
                String day = dateList.get(i);
                String[] sday = day.split("-");
                int year = Integer.parseInt(sday[0]);
                int month = Integer.parseInt(sday[1]) ;
                int dday = Integer.parseInt(sday[2]);
                final CalendarDay deco_date = CalendarDay.from(year, month, dday);
                DayViewDecorator deco = new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        return deco_date != null && day.equals(deco_date);
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.addSpan(new ForegroundColorSpan(Color.parseColor("#8E44AD")));
                    }
                };
                decoList.add(deco);
            }

            widget.addDecorators(decoList);

            widget.setOnDateChangedListener(this);

            return new android.app.AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try{
                                listener.DateChanged(selected_date_str);
                            }catch (Exception e){
                                Log.e("TEST", e.toString());
                            }

                        }
                    })
                    .create();
        }

        public void setOnDataChangedMyListener(DateChangedListener listener){
            this.listener = listener;
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            try{
                listener = (DateChangedListener) context;
            }catch (Exception e){

            }
        }

        @Override
        public void onDateSelected(
                MaterialCalendarView widget,
                CalendarDay date,
                boolean selected) {
            int year = date.getYear();
            int month = date.getMonth();
            int day = date.getDay();
            //String selectedDateStr = String.format("%d-%d-%d", year, month, day);
            selected_date_str = String.format("%d-%d-%d", year, month, day);


        }

    }



}
