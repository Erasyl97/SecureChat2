package com.example.erabe_000.securechat2;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MessagesActivity extends AppCompatActivity {

    Button btnSendMsg;
    EditText etMsg;
    private FirebaseAuth mAuth;
    ListView lvDiscussion;
    ArrayList<String> listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    String UserName, user_msg_key, my_id, talkingUserID, UserFullName, publicRsaKey, myPublicRsaKey;

    //KeyPair pair; //////

    String userid;

    private DatabaseReference dbr;
    private DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference("users");
    User user1;

    //db
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        dataBase = new DataBase(this);
        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        etMsg = (EditText) findViewById(R.id.etMessage);
        mAuth = FirebaseAuth.getInstance();
        //
        userid = getIntent().getExtras().get("userid").toString();

        //
        lvDiscussion = (ListView) findViewById(R.id.lvConversation);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listConversation);
        lvDiscussion.setAdapter(arrayAdpt);

        dbr2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    user1 = ds.getValue(User.class);

                    if ((user1.getUserid()).equals(userid))
                    {
                        talkingUserID = user1.getUserid();
                        setTitle(user1.getUserFullname());
                        UserName = user1.getUsername();
                        UserFullName = user1.getUserFullname();
                        publicRsaKey = user1.getPublicRSAKey();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        my_id = mAuth.getUid().toString();

        dbr = FirebaseDatabase.getInstance().getReference().child("messages");



        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                //Encripting the message and sending AES key encrypted by RSA
                SharedPreferences prefs = getSharedPreferences("AESsecretKeys", MODE_PRIVATE);
                String restoredInfo = prefs.getString("text", null);
                String secretAESKey = "";
                if (restoredInfo!=null) {
                    secretAESKey = prefs.getString(mAuth.getUid().toString(), "");
                }

                byte[] dec = android.util.Base64.decode(publicRsaKey, android.util.Base64.DEFAULT);
                String aesEnWithRsa = "";
                try {
                    PublicKey key2 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(dec));
                    aesEnWithRsa = RSA.encrypt(secretAESKey, key2);
                } catch (Exception ex) {

                }

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("msg", AES.encrypt(etMsg.getText().toString(), secretAESKey));
                map2.put("user_msg_key", user_msg_key);
                map2.put("toid", talkingUserID);
                map2.put("fromid", my_id);
                map2.put("encAESkey",aesEnWithRsa);
                dbr2.updateChildren(map2);

                etMsg.setText("");
            }
        });





        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateConversation(DataSnapshot dataSnapshot){
        String msg, toid, from_id, conversation, user_msg_keyy, encAesKey;

        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            encAesKey = (String) ((DataSnapshot)i.next()).getValue();
            from_id = (String) ((DataSnapshot)i.next()).getValue();
            msg = (String) ((DataSnapshot)i.next()).getValue();
            toid = (String) ((DataSnapshot)i.next()).getValue();
            user_msg_keyy = (String) ((DataSnapshot)i.next()).getValue();

            //Decrypting AES
            SharedPreferences prefs = getSharedPreferences("AESsecretKeys", MODE_PRIVATE);
            String restoredInfo = prefs.getString("text", null);
            String secretAESKey = "";
            if (restoredInfo!=null) {
                secretAESKey = prefs.getString(mAuth.getUid().toString(), "");
            }
            //RSA AES
            SharedPreferences prefs2 = getSharedPreferences("RSAprivateKeys", MODE_PRIVATE);
            String restoredInfo2 = prefs2.getString("text", null);
            String privateRsaKey = "";
            if (restoredInfo2!=null) {
                privateRsaKey = prefs2.getString(mAuth.getUid().toString(), "");
            }
            byte[] dec = android.util.Base64.decode(privateRsaKey, android.util.Base64.DEFAULT);
            String AESkey="";
            try {
                PrivateKey key2 = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(dec));
                AESkey = RSA.decrypt(encAesKey,key2);
            } catch (Exception ex) {

            }


            if (from_id.equals(my_id) && toid.equals(talkingUserID)) {
                conversation = "Me: " + AES.decrypt(msg,secretAESKey);
                arrayAdpt.insert(conversation, arrayAdpt.getCount());
            }
            if (from_id.equals(talkingUserID) && toid.equals(my_id)) {
                conversation = UserName + ": " + AES.decrypt(msg, AESkey);
                arrayAdpt.insert(conversation, arrayAdpt.getCount());
            }
            arrayAdpt.notifyDataSetChanged();
        }


    }
}
