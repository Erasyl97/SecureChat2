package com.example.erabe_000.securechat2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class UserProfile extends AppCompatActivity {

    User user1;
    String userName, userSurname, tel;
    String email,userid;
    TextView namePlace, emailPlace, telPlace, profilePlace;
    Button startChat;
    private FirebaseAuth mAuth;
    private DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profilePlace = findViewById(R.id.userNameProfile);
        namePlace = findViewById(R.id.textView31);
        emailPlace = findViewById(R.id.textView41);
        telPlace = findViewById(R.id.textView51);
        mAuth = FirebaseAuth.getInstance();
        userid = getIntent().getExtras().get("userid").toString();
        startChat = findViewById(R.id.startChat);

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, MessagesActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });

        dbr2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    user1 = ds.getValue(User.class);
                    if ((user1.getUserid()).equals(userid))
                    {
                        userName = user1.getUsername();
                        userSurname = user1.getUserSurname();
                        email = user1.getEmail();
                        tel = user1.getTel();
                        namePlace.setText(userName + " " + userSurname);
                        telPlace.setText(tel);
                        profilePlace.setText(userName + "'s Profile");
                        emailPlace.setText(email);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            Intent intent = new Intent(UserProfile.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.messages) {
            Intent intent = new Intent(UserProfile.this, UsersActivity.class);
            startActivity(intent);
        }
        if (id == R.id.users) {
            //Intent intent = new Intent(UserProfile.this, UserList.class);
            //startActivity(intent);
            finish();
        }
        if (id == R.id.profile) {
            Intent intent = new Intent(UserProfile.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.doctors) {
            Intent intent = new Intent(UserProfile.this, DoctorListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.storage) {
            Intent intent = new Intent(UserProfile.this, StorageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
