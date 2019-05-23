package com.example.erabe_000.securechat2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UserList extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ListView users;

    UserListAdapter userListAdapter;
    ArrayList<User> users1 = new ArrayList<User>();

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("users");
    /*FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;*/
    User user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mAuth = FirebaseAuth.getInstance();
        users = (ListView) findViewById(R.id.listView1);

        userListAdapter = new UserListAdapter(this,R.layout.list_users,users1);
        users.setAdapter(userListAdapter);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> userSet = new ArrayList();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    user1 = ds.getValue(User.class);
                    if (!(user1.getUserid()).equals(mAuth.getUid().toString())) {
                        userSet.add(user1);
                    }
                }
                userListAdapter.clear();
                userListAdapter.addAll(userSet);
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),UserProfile.class);
                User selectedUser = (User)parent.getItemAtPosition(position);
                i.putExtra("userid", selectedUser.getUserid());
                startActivity(i);
            }
        };
        users.setOnItemClickListener(itemListener);

        /*
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listOfUsers);
        //arrayAdapter = new ArrayAdapter(this, R.layout.user_info, R.id.userInfo,listOfUsers);
        users.setAdapter(arrayAdapter);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                String username;
                String userSurname;
                String name;

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    user1 = ds.getValue(User.class);
                    if (!(user1.getUserid()).equals(mAuth.getUid().toString())) {
                        username = user1.getUsername();
                        sendName = username;
                        sendUserid = user1.getUserid();
                        userSurname = user1.getUserSurname();
                        name = username + " " + userSurname;
                        set.add(name);
                    }
                }
                nameSet = set;
                arrayAdapter.clear();
                arrayAdapter.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),UserProfile.class);
                //i.putExtra("username", sendName);
                i.putExtra("userid", sendUserid);
                startActivity(i);
            }
        };
        users.setOnItemClickListener(itemListener);*/
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
            Intent intent = new Intent(UserList.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        if (id == R.id.messages) {
            Intent intent = new Intent(UserList.this, UsersActivity.class);
            startActivity(intent);
        }
        if (id == R.id.users) {
            Toast.makeText(UserList.this, "Users Page", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.profile) {
            Intent intent = new Intent(UserList.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.doctors) {
            Intent intent = new Intent(UserList.this, DoctorListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.storage) {
            Intent intent = new Intent(UserList.this, StorageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
