package com.zulfi.sema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;


public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;

//    private GoogleSignInClient googleClient;
//    private FirebaseAuth mAuth;
//    private static int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.firebase_web_client_id))
//                .requestEmail()
//                .build();
//
//        googleClient = GoogleSignIn.getClient(this, gso);
//        mAuth = FirebaseAuth.getInstance();

        // masih manual
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();
        // YANG DIKASIH KOMENTAR DARI GOOGLE LOGIN
//        final FirebaseUser user = mAuth.getCurrentUser();

        final Boolean isLogin = mPreferences.getBoolean(getString(R.string.pref_is_login_key), false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
//                if (user != null) {
                if (isLogin) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
