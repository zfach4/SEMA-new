package com.zulfi.sema;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    String name;
    String telp;

    public Users(){}

    public Users(String name, String telp) {
        this.name = name;
        this.telp = telp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
}
