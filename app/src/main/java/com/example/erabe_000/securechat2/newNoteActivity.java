package com.example.erabe_000.securechat2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class newNoteActivity extends AppCompatActivity {

    EditText noteAbout, noteDescription;
    Button addPicture, createNote;
    ImageView notePicture;

    final int REQUEST_CODE_GALLERY = 999;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        noteAbout = findViewById(R.id.noteAbout);
        noteDescription = findViewById(R.id.noteDescription);
        notePicture = findViewById(R.id.imageView2);
        addPicture = findViewById(R.id.addPhotoButton);
        createNote = findViewById(R.id.createButton);

        mAuth = FirebaseAuth.getInstance();

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(newNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime());
                    byte[] bytes = pictureToByte(notePicture);

                    Cursor cursor = StorageActivity.sqLiteHelper.getData("SELECT * FROM NOTE");
                    int id = cursor.getCount()+1;

                    Bitmap bitmap = ((BitmapDrawable)notePicture.getDrawable()).getBitmap();
                    File file;
                    String path = Environment.getExternalStorageDirectory().toString();
                    file = new File(path, "imageNote" + id +".jpg");
                    try{
                        OutputStream stream = null;
                        stream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        stream.flush();
                        stream.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    //Encrypting data with AES;
                    SharedPreferences prefs = getSharedPreferences("AESsecretKeys", MODE_PRIVATE);
                    String restoredInfo = prefs.getString("text", null);
                    String secretAESKey = "";
                    if (restoredInfo!=null) {
                        secretAESKey = prefs.getString(mAuth.getUid().toString(), "");
                    }

                   StorageActivity.sqLiteHelper.insertData(
                            AES.encrypt(noteAbout.getText().toString().trim(), secretAESKey) ,
                            AES.encrypt(noteDescription.getText().toString().trim(), secretAESKey),
                            file.getAbsolutePath(),
                            AES.encrypt(timeStamp, secretAESKey)
                    );
                    Toast.makeText(getApplicationContext(), "New Note Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(newNoteActivity.this, StorageActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private byte[] pictureToByte(ImageView picture) {
        Bitmap bitmap = ((BitmapDrawable)picture.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode== RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                notePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
