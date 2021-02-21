package com.course.traveldiary;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EditDiaryActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    Uri mImageCaptureUri;
    ImageView diaryPhoto;
    Button image_upload_btn;
    TextView d_date;
    ImageView photo;
    EditText place, memo;

    String date;
    int id;

    private int id_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_diary_layout);
        setTitle("EDIT DIARY");

        Intent intent = getIntent();
        date = intent.getExtras().getString("date");
        id = intent.getExtras().getInt("id");
        Log.i("parse",date + " " + id);

        d_date = (TextView)findViewById(R.id.diary_date);
        diaryPhoto = (ImageView) this.findViewById(R.id.diary_image);
        image_upload_btn = (Button)this.findViewById(R.id.image_upload_btn);
        place = (EditText)this.findViewById(R.id.diary_place);
        memo = (EditText)this.findViewById(R.id.diary_memo);
        photo = (ImageView)this.findViewById(R.id.diary_image);

        d_date.setText(date);
        //diaryPhoto.setImageResource(R.drawable.image_icon);
        image_upload_btn = (Button)this.findViewById(R.id.image_upload_btn);
        image_upload_btn.setOnClickListener(this);

        if(dbHelper.getGPSpColor(id).contains("blue")){
            String d_memo = dbHelper.getDiaryMemo(id);
            String d_place = dbHelper.getDiaryPlace(id);
            String d_imgpath = dbHelper.getDiaryImg(id);
            Log.i("mmm",d_memo+" "+d_place+" "+d_imgpath);
            memo.setText(d_memo);
            place.setText(d_place);

            File imgFile = new File(d_imgpath);
            if(imgFile.exists()){
                Log.i("mmm","exists");
            }
            Bitmap bitmap = decodeFile(imgFile.getAbsolutePath());
            diaryPhoto.setImageBitmap(bitmap);
        }

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_diary_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_save_btn:
                saveBtnOnClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void saveBtnOnClicked() {
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "TravelDiary.db", null, 1);

        EditText d_place = (EditText) findViewById(R.id.diary_place);
        EditText d_memo = (EditText)findViewById(R.id.diary_memo);

        String img_path = "";
        String place;
        String memo;
        try {
            img_path = getPath(mImageCaptureUri);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(img_path == ""){
            Toast.makeText(this, "Please choose photo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(d_place.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please write place", Toast.LENGTH_SHORT).show();
            return;

        }
        if(d_memo.getText().toString().isEmpty()){
            Toast.makeText(this, "Please write memo", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            place = d_place.getText().toString();
            memo = d_memo.getText().toString();

            dbHelper.update_diary(id, img_path, place, memo);
            dbHelper.update_GPS(id, "blue");

            Toast.makeText(getApplicationContext(), "Saved Diary", Toast.LENGTH_SHORT).show();
            Log.i("save diary",dbHelper.getResult_diary());

        }

    }

    //카메라에서 사진 촬영
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    //앨범에서 이미지 가져오기
    public void doTakeAlbumAction(){
        //앨범호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch (requestCode){
            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();

                Log.d("img",  getPath(mImageCaptureUri));

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                    diaryPhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case PICK_FROM_CAMERA:
            {
                if(resultCode != RESULT_OK){
                    return;
                }
                mImageCaptureUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    diaryPhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        id_view = v.getId();
        if(v.getId() == R.id.image_upload_btn){
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doTakeAlbumAction();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지를 선택하세요.")
                    .setPositiveButton("카메라",cameraListener)
                    .setNeutralButton("갤러리",albumListener)
                    .setNegativeButton("Cancel",cancelListener)
                    .show();
        }
    }

    public String getPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
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
