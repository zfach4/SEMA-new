package com.zulfi.sema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText et_username, et_password;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.title_activity_login);
        initView();
        setData();
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
}