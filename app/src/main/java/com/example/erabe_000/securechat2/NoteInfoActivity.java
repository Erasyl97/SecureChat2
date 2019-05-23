package com.example.erabe_000.securechat2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

public class NoteInfoActivity extends AppCompatActivity {

    TextView noteAbout, noteTime, noteDescription;
    ImageView notePicture;

    private FirebaseAuth mAuth;
    int noteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info);

        noteAbout = findViewById(R.id.noteAbout);
        noteTime = findViewById(R.id.noteTime);
        noteDescription = findViewById(R.id.noteDescription);
        notePicture = (ImageView) findViewById(R.id.imageView2);

        mAuth = FirebaseAuth.getInstance();

        noteID = getIntent().getExtras().getInt("noteID");

        Cursor cursor = StorageActivity.sqLiteHelper.getData("SELECT * FROM NOTE WHERE id='" + noteID + "'");

        Note note;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String about = cursor.getString(1);
            String description = cursor.getString(2);
            String picture = cursor.getString(3);
            String time = cursor.getString(4);

            //AES secret key
            SharedPreferences prefs = getSharedPreferences("AESsecretKeys", MODE_PRIVATE);
            String restoredInfo = prefs.getString("text", null);
            String secretAESKey = "";
            if (restoredInfo!=null) {
                secretAESKey = prefs.getString(mAuth.getUid().toString(), "");
            }


            note = new Note(about, description, time, picture, id);
            noteAbout.setText(AES.decrypt(note.getAbout(), secretAESKey));
            noteTime.setText(AES.decrypt(note.getTime(), secretAESKey));
            noteDescription.setText(AES.decrypt(note.getDescription(), secretAESKey));


            File file = new File(picture);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            notePicture.setImageBitmap(bitmap);
     }

    }
}
