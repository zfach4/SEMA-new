package com.zulfi.sema;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

public class FireApp extends Application {

    public FireApp() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
