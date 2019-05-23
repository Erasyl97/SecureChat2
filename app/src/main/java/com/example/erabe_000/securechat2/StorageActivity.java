package com.example.erabe_000.securechat2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class StorageActivity extends AppCompatActivity {

    Button addNote;
    private FirebaseAuth mAuth;

    GridView gridView;
    ArrayList<Note> list;
    NoteListAdapter adapter = null;

    int selectedNoteID;

    public static SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        addNote = findViewById(R.id.btnAdd);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new NoteListAdapter(this, R.layout.note_item, list);
        gridView.setAdapter(adapter);
        sqLiteHelper = new SQLiteHelper(this, "NoteDB.sqlite", null, 1);
        //sqLiteHelper.queryData("DROP TABLE NOTE");
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS NOTE(Id INTEGER PRIMARY KEY AUTOINCREMENT, about VARCHAR, description VARCHAR, picture VARCHAR, currentTime VARCHAR)");

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM NOTE");
        list.clear();

        //AES secret key
        SharedPreferences prefs = getSharedPreferences("AESsecretKeys", MODE_PRIVATE);
        String restoredInfo = prefs.getString("text", null);
        String secretAESKey = "";
        if (restoredInfo!=null) {
            secretAESKey = prefs.getString(mAuth.getUid().toString(), "");
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String about = cursor.getString(1);
            String description = cursor.getString(2);
            String picture = cursor.getString(3);
            String time = cursor.getString(4);

            list.add(new Note(AES.decrypt(about,secretAESKey), description, AES.decrypt(time, secretAESKey), picture, id));
        }
        //adapter.notifyDataSetChanged();

        // finding taped note id

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Cursor c = sqLiteHelper.getData("SELECT id FROM NOTE");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                selectedNoteID = arrID.get(position);
                Intent intent = new Intent(StorageActivity.this, NoteInfoActivity.class);
                intent.putExtra("noteID", selectedNoteID);
                startActivity(intent);
                return false;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = sqLiteHelper.getData("SELECT id FROM NOTE");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                selectedNoteID = arrID.get(position);
                Intent intent = new Intent(StorageActivity.this, NoteInfoActivity.class);
                intent.putExtra("noteID", selectedNoteID);
                startActivity(intent);
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StorageActivity.this, newNoteActivity.class);
                startActivity(intent);
                finish();
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
            Intent intent = new Intent(StorageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.messages) {
            Intent intent = new Intent(StorageActivity.this, UsersActivity.class);
            startActivity(intent);
        }
        if (id == R.id.users) {
            Intent intent = new Intent(StorageActivity.this, UserList.class);
            startActivity(intent);
        }
        if (id == R.id.storage) {
            Toast.makeText(StorageActivity.this, "Your storage", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.doctors) {
            Intent intent = new Intent(StorageActivity.this, DoctorListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.profile) {
            Intent intent = new Intent(StorageActivity.this, ProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
