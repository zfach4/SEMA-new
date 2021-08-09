package com.zulfi.sema;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText et_username, et_password;
    private String email, password;

    private FirebaseAuth mAuth;
    private static int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login);

        setTitle(R.string.title_activity_login);
        initView();
        setData();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                // ambil data dari text view
                email = et_username.getText().toString();
                password = et_password.getText().toString();

                // validasi login
                if (email.equals("")){
                    et_username.setError("Email tidak boleh kosong");
                } else if (password.equals("")){
                    et_password.setError("Password tidak boleh kosong");
                }
                else{
                    // action
                    doLogin(email, password);
                }
            }
        });
    }

    private void saveUserLoginInfo(String email, String name, String UID) {
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
                .putString(getString(R.string.pref_user_fullname_key), name)
                .apply();
        // save image url untuk user's profile photo
        if (UID != null){
            mPreferences
                    .edit()
                    .putString(getString(R.string.pref_uid_key), UID)
                    .apply();
        }
        // save email
        mPreferences
                .edit()
                .putString(getString(R.string.pref_user_email_key), email)
                .apply();
    }

    public void doLogin (String email, String password ){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Zulfi Firebase", "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Login Berhasil.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            saveUserLoginInfo(user.getEmail(), user.getDisplayName(), user.getUid());
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Zulfi Firebase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login Gagal.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }
}