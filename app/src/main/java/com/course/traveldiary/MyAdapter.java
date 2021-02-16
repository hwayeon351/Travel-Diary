package com.course.traveldiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView myPicture;
        TextView myPlace, myDate, myMemo;

        MyViewHolder(View view){
            super(view);
            myPicture = view.findViewById(R.id.diary_image);
            myPlace = view.findViewById(R.id.diary_place);
            myDate = view.findViewById(R.id.diary_date);
            myMemo = view.findViewById(R.id.diary_memo);
        }
    }

    private ArrayList<Diary> myDiarylist;

    MyAdapter(ArrayList<Diary> diaries){
        this.myDiarylist = diaries;
    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position){
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        String img_path = myDiarylist.get(position).getImg_path();
        File file = new File(img_path);
        if(file.exists()){
            Bitmap bitmap = decodeFile(file.getAbsolutePath());
            myViewHolder.myPicture.setImageBitmap(bitmap);
        }
        myViewHolder.myDate.setText(myDiarylist.get(position).getDate());
        myViewHolder.myMemo.setText(myDiarylist.get(position).getMemo());
        myViewHolder.myPlace.setText(myDiarylist.get(position).getPlace());

    }

    @Override
    public int getItemCount(){
        return myDiarylist.size();
    }

    private Bitmap decodeFile(String imgPath){
        Bitmap b = null;
        int max_size = 1000;
        File f = new File(imgPath);
        try{
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis,null,o);
            fis.close();
            int scale = 1;
            if(o.outHeight > max_size || o.outWidth > max_size){
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(max_size / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis,null,o2);
            fis.close();
        }catch (Exception e){
            Log.e("Error",e.toString());
        }
        Log.i("Info", "Width :  "+ b.getWidth() + " , Height : " + b.getHeight());
        return b;
    }
}