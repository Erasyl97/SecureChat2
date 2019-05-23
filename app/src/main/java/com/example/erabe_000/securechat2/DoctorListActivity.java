package com.example.erabe_000.securechat2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
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

public class DoctorListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ListView doctors;

    DoctorListAdapter doctorListAdapter;
    ArrayList<Doctor> doctors1 = new ArrayList<Doctor>();
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("doctors");

    Doctor doctor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        mAuth = FirebaseAuth.getInstance();
        doctors = (ListView) findViewById(R.id.listView1);

        doctorListAdapter = new DoctorListAdapter(this,R.layout.list_users, doctors1);
        doctors.setAdapter(doctorListAdapter);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Doctor> userSet = new ArrayList();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    doctor1 = ds.getValue(Doctor.class);
                    if (!(doctor1.getUserid()).equals(mAuth.getUid().toString())) {
                        userSet.add(doctor1);
                    }
                }
                doctorListAdapter.clear();
                doctorListAdapter.addAll(userSet);
                doctorListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),DoctorsProfileActivity.class);
                Doctor selectedDoctor = (Doctor)parent.getItemAtPosition(position);
                i.putExtra("userid", selectedDoctor.getUserid());
                startActivity(i);
            }
        };
        doctors.setOnItemClickListener(itemListener);
    }

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
            Intent intent = new Intent(DoctorListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        if (id == R.id.messages) {
            Intent intent = new Intent(DoctorListActivity.this, UsersActivity.class);
            startActivity(intent);
        }
        if (id == R.id.doctors) {
            Toast.makeText(DoctorListActivity.this, "Doctors Page", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.users) {
            Intent intent = new Intent(DoctorListActivity.this, UserList.class);
            startActivity(intent);
        }
        if (id == R.id.profile) {
            Intent intent = new Intent(DoctorListActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.storage) {
            Intent intent = new Intent(DoctorListActivity.this, StorageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
