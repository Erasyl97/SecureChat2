package com.example.erabe_000.securechat2;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RegisterDoctorActivity extends AppCompatActivity {

    EditText nameRegister, emailRegister, doctorsSpecialzation, doctorsDescription, passwordRegister, surnameRegister, telRegister, conPassword;
    Button registerButton;
    KeyPair pair;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        mAuth = FirebaseAuth.getInstance();

        //


        nameRegister = findViewById(R.id.name_register);
        surnameRegister = findViewById(R.id.surname_register);
        telRegister = findViewById(R.id.tel_register);
        emailRegister = findViewById(R.id.email_register);
        passwordRegister = findViewById(R.id.password_register);
        registerButton = findViewById(R.id.submit_register);
        doctorsSpecialzation = findViewById(R.id.specialization);
        doctorsDescription = findViewById(R.id.description);
        conPassword = findViewById(R.id.confirm_password);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameRegister.getText().toString();
                final String surname = surnameRegister.getText().toString();
                final String specialization = doctorsSpecialzation.getText().toString();
                final String description = doctorsDescription.getText().toString();
                final String tel = telRegister.getText().toString();
                final String email = emailRegister.getText().toString();
                String password = passwordRegister.getText().toString();
                String conpassword = conPassword.getText().toString();

                PublicKey key1 = pair.getPublic();
                PrivateKey key2 = pair.getPrivate();

                byte[] publicRsaKeyByte = key1.getEncoded();
                byte[] privateRsaKeyByte = key2.getEncoded();

                final String publicRsaKey = Base64.encodeToString(publicRsaKeyByte, Base64.DEFAULT);
                final String privateRsaKey = Base64.encodeToString(privateRsaKeyByte, Base64.DEFAULT);
                if (password.equals(conpassword)) {
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterDoctorActivity.this,"Success", Toast.LENGTH_SHORT).show();

                                    String secretKey = randomGenerate(30);
                                    SharedPreferences.Editor editor = getSharedPreferences("AESsecretKeys", MODE_PRIVATE).edit();
                                    editor.putString(mAuth.getUid().toString(),secretKey);

                                    //RSA
                                    SharedPreferences.Editor editor1 = getSharedPreferences("RSAprivateKeys", MODE_PRIVATE).edit();
                                    editor1.putString(mAuth.getUid().toString(), privateRsaKey);

                                    Doctor doctor = new Doctor(name, surname, tel, email, mAuth.getUid(), specialization, description, publicRsaKey);
                                    mDatabase.child("doctors").child(mAuth.getUid()).setValue(doctor);
                                    Intent intent = new Intent(RegisterDoctorActivity.this, DoctorProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(RegisterDoctorActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(RegisterDoctorActivity.this,"Wrong password", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    public String randomGenerate(int x) {
        String stringSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz" + "0123456789,.-_!@#$";

        StringBuilder sb = new StringBuilder(x);

        for (int i = 0; i < x; i++) {
            int index = (int)(stringSet.length() * Math.random());

            sb.append(stringSet.charAt(index));
        }
        return sb.toString();
    }
}
