package com.zulfi.sema;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_check;
    private TextView tv_servo;
    private EditText et_username, et_password;
    private String username, password;

//    private Firebase mRefServo;
    DatabaseReference mRefServo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login);

        setTitle(R.string.title_activity_login);
        initView();
        setData();
        loadFirebase();

    }

    @Override
    public void onBackPressed() {
        // untuk disable button back
        return;
    }

    public void initView(){
        btn_login = findViewById(R.id.btn_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        //firebase inisialisasi
//        mRefServo = new Firebase("https://smartcity-gbaz-ae18-default-rtdb.firebaseio.com/SmartCityAE18/Servo");
        tv_servo = findViewById(R.id.firebase_servo);
//        btn_check = (Button) findViewById(R.id.btn_check);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRefServo = database.getReference("SmartCityAE18").child("Servo");

    }

    public void setData(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                password = et_password.getText().toString();

                // kalau berhasil login, panggil ini:
                saveUserLoginInfo(username);

                // action
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
//        btn_check.setOnClickListener(new android.view.View.OnClickListener(){
//
//            @Override
//            public void onClick(android.view.View view) {
//                mRefServo.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        String v = dataSnapshot.getValue(String.class);
//                        tv_servo.setText(v);
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//            }
//        });
    }

    private void saveUserLoginInfo(String username) {
        // save ke SharedPreferences (berhasil login)
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // isLogin = true
        mPreferences
                .edit()
                .putBoolean(getString(R.string.pref_is_login_key), true)
                .apply();
        // save nama
        mPreferences
                .edit()
                .putString(getString(R.string.pref_user_fullname_key), username)
                .apply();
        // save image url untuk user's profile photo
        mPreferences
                .edit()
                .putString(getString(R.string.pref_user_image_url), "@drawable/cute.png")
                .apply();
        // save email
        String email = username.replaceAll("\\s", "").toLowerCase() + "@gmail.com";
        mPreferences
                .edit()
                .putString(getString(R.string.pref_user_email_key), email)
                .apply();
    }

//    private void loadFirebase() {
//        mRefServo.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String nilai = dataSnapshot.getValue(String.class);
//                tv_servo.setText(nilai);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }

    private void loadFirebase() {
        mRefServo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nilai = snapshot.getValue(String.class);

                tv_servo.setText("servo zulfi " + nilai);
                Log.d("Zulfi Firebase", "Value is " + nilai);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Zulfi Firebase", "Failed to read value", error.toException());
            }
        });

    }
}