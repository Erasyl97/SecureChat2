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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ListView users;
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("users");
    User user1;

    UserListAdapter userListAdapter;
    ArrayList<User> users1 = new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mAuth = FirebaseAuth.getInstance();
        users = (ListView) findViewById(R.id.listView);

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
                Intent i = new Intent(getApplicationContext(),MessagesActivity.class);
                User selectedUser = (User)parent.getItemAtPosition(position);
                i.putExtra("userid", selectedUser.getUserid());
                i.putExtra("username", selectedUser.getUsername());
                i.putExtra("myid", mAuth.getUid());
                startActivity(i);
            }
        };
        users.setOnItemClickListener(itemListener);


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
            Intent intent = new Intent(UsersActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        if (id == R.id.messages) {
            Toast.makeText(UsersActivity.this, "Messages Page", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.users) {
            Intent intent = new Intent(UsersActivity.this, UserList.class);
            startActivity(intent);
        }
        if (id == R.id.profile) {
            Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.doctors) {
            Intent intent = new Intent(UsersActivity.this, DoctorListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.storage) {
            Intent intent = new Intent(UsersActivity.this, StorageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
