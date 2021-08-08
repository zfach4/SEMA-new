package com.zulfi.sema;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    String nama;
    String telp;

    public Users(){}

    public Users(String nama, String telp) {
        this.nama = nama;
        this.telp = telp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
}
