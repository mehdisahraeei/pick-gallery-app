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
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;



public class MainActivity extends AppCompatActivity {

    private Button button1, button2;
    private ImageView imageView;
    private TextView textView;
    private int Request_File = 1;
    private int Request_Storage = 111;
    private Uri uri;
    private String stringPath;
    private OutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        imageView = findViewById(R.id.image1);
        textView = findViewById(R.id.tone);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PathActivity.class));
            }
        });



        button1.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Request_File);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Request_File && resultCode == RESULT_OK && data != null && data.getData() != null) {


            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath() + "/lib/");
            dir.mkdirs();
            File file = new File(dir, "one.JPEG");

            uri = data.getData();
            getStringPath(uri);


            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);

            outputStream = null;


            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(MainActivity.this, "save", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
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