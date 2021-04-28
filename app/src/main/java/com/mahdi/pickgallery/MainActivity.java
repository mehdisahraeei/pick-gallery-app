package com.mahdi.pickgallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;
    private int Request_File = 222;
    private int Request_Storage = 111;
    private Uri uri;
    private String stringPath;
    private Intent Data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn1);
        imageView = findViewById(R.id.image1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Request_Storage);
                } else {
                    selectImage();
                }

            }
        });


    }






    private void selectImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), Request_File);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Request_File && resultCode == RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                Data = data;

                getStringPath(uri);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }




    public String getStringPath(Uri uriN) {
        String[] filepath = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uriN, filepath, null, null, null);
        if (cursor == null) {
            stringPath = uriN.getPath();
        } else {
            cursor.moveToFirst();
            int ColumnIndex = cursor.getColumnIndex(filepath[0]);
            stringPath = cursor.getString(ColumnIndex);
            cursor.close();
        }
        return stringPath;
    }



}