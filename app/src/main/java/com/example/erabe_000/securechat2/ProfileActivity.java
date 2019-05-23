package com.example.erabe_000.securechat2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    User user1;
    String userName, userSurname, tel;
    String email;
    TextView namePlace, emailPlace, telPlace;
    Button setImage;
    Boolean isDoctor = true;
    ImageView imagePlace;
    private FirebaseAuth mAuth;
    private DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference dbr3 = FirebaseDatabase.getInstance().getReference("profileImages");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        namePlace = findViewById(R.id.textView3);
        emailPlace = findViewById(R.id.textView4);
        telPlace = findViewById(R.id.textView5);
        mAuth = FirebaseAuth.getInstance();
        setImage = findViewById(R.id.setImageButton);
        imagePlace = findViewById(R.id.imagePlace);


        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileImageActivity.class);
                startActivity(intent);
            }
        });
        dbr2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    user1 = ds.getValue(User.class);
                    if ((user1.getUserid()).equals(mAuth.getUid().toString()))
                    {
                        userName = user1.getUsername();
                        userSurname = user1.getUserSurname();
                        email = user1.getEmail();
                        tel = user1.getTel();
                        namePlace.setText(userName + " " + userSurname);
                        telPlace.setText(tel);
                        emailPlace.setText(email);
                        isDoctor = false;
                        break;
                    }
                }
                if (isDoctor) {
                    Intent intent = new Intent(ProfileActivity.this, DoctorProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbr3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String imageBitmap;
                    while (i.hasNext()) {
                        try {
                            imageBitmap = (String)((DataSnapshot)i.next()).getValue();
                            Bitmap bitmap = StringToBitMap(imageBitmap);
                            imagePlace.setImageBitmap(bitmap);
                            break;
                        } catch (Exception ex) {

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mAuth = FirebaseAuth.getInstance();
        if (id == R.id.menu_sign_out) {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.messages) {
            Intent intent = new Intent(ProfileActivity.this, UsersActivity.class);
            startActivity(intent);
        }
        if (id == R.id.users) {
            Intent intent = new Intent(ProfileActivity.this, UserList.class);
            startActivity(intent);
        }
        if (id == R.id.profile) {
            Toast.makeText(ProfileActivity.this, "Profile Page", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.doctors) {
            Intent intent = new Intent(ProfileActivity.this, DoctorListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.storage) {
            Intent intent = new Intent(ProfileActivity.this, StorageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
