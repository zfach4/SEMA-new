package com.zulfi.sema.smart_light;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SmartLightViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    // variable realtime - dapatkan value
    private MutableLiveData<String> lamp1;
    private MutableLiveData<String> lamp2;
    private MutableLiveData<String> sumberAC;
    private MutableLiveData<String> kwh;
    private MutableLiveData<Boolean> mode;

    // alamat database untuk tiap data item di firebase
    private DatabaseReference lamp1GetterRef;
    private DatabaseReference lamp1SetterRef;
    private DatabaseReference lamp2GetterRef;
    private DatabaseReference lamp2SetterRef;
    private DatabaseReference sumberACRef;
    private DatabaseReference kwhRef;
    private DatabaseReference modeGetterRef;
    private DatabaseReference modeSetterRef;

    // listener
    private ValueEventListener lamp1Listener;
    private ValueEventListener lamp2Listener;
    private ValueEventListener sumberACListener;
    private ValueEventListener kwhListener;
    private ValueEventListener modeListener;

    public SmartLightViewModel(){
        // Sensor
        DatabaseReference sensorRef = FirebaseDatabase.getInstance().getReference("DataUpdateTerakhir");
        sumberACRef = sensorRef.child("SumberListrikAC");
        kwhRef = sensorRef.child("pzemValue");
        lamp1GetterRef = sensorRef.child("Lampu1");
        lamp2GetterRef = sensorRef.child("Lampu2");
        modeGetterRef = sensorRef.child("Mode");
        DatabaseReference controlRef = FirebaseDatabase.getInstance().getReference("Control");
        // Sensor children: Aki, vPanel, aPanel, Azimuth, PosisiServo
        lamp1SetterRef = controlRef.child("Lampu1");
        lamp2SetterRef = controlRef.child("Lampu2");
        modeSetterRef = controlRef.child("Mode");

    }

    // yang harus dilakukan ketika class ini akan dihapus
    // untuk bersih-bersih agar sebagian proses yang tidak dipakai tidak membebani aplikasi
    @Override
    protected void onCleared() {
        // hapus listener bila sudah di-create

        if (lamp1Listener != null) {
            lamp1GetterRef.removeEventListener(lamp1Listener);
        }

        if (lamp2Listener != null) {
            lamp2GetterRef.removeEventListener(lamp2Listener);
        }

        if (modeListener != null) {
            modeGetterRef.removeEventListener(modeListener);
        }

        if (sumberACListener != null) {
            sumberACRef.removeEventListener(sumberACListener);
        }

        if (kwhListener != null) {
            kwhRef.removeEventListener(kwhListener);
        }
    }

    public MutableLiveData<String> getLamp1() {
        if (lamp1 == null) {
            lamp1 = new MutableLiveData<>();
            // read data lamp1 dari firebase
            lamp1Listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai lamp1 dari firebase ke tipe data String
                    String nilaiLamp1 = snapshot.getValue(String.class);
                    // kasih nilaiLamp1 ke lamp1 untuk dikirim ke SmartLightFragment
                    lamp1.setValue(nilaiLamp1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data Lamp1 dari firebase", error.toException());
                }
            };
            lamp1GetterRef.addValueEventListener(lamp1Listener);
        }
        return lamp1;
    }

    public MutableLiveData<String> getLamp2() {
        if (lamp2 == null) {
            lamp2 = new MutableLiveData<>();
            // read data lamp2 dari firebase
            lamp2Listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai lamp2 dari firebase ke tipe data String
                    String nilaiLamp2 = snapshot.getValue(String.class);
                    // kasih nilaiLamp2 ke lamp1 untuk dikirim ke SmartLightFragment
                    lamp2.setValue(nilaiLamp2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data lamp2 dari firebase", error.toException());
                }
            };
            lamp2GetterRef.addValueEventListener(lamp2Listener);
        }
        return lamp2;
    }

    public MutableLiveData<String> getSumberAC() {
        if (sumberAC == null) {
            sumberAC = new MutableLiveData<>();
            // read data sumberAC dari firebase
            sumberACListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai sumberAC dari firebase ke tipe data String
                    String nilaiSumberAC = snapshot.getValue(String.class);
                    // kasih nilaiSumberAC ke sumberAC untuk dikirim ke SmartLightFragment
                    sumberAC.setValue(nilaiSumberAC);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data sumberAC dari firebase", error.toException());
                }
            };
            sumberACRef.addValueEventListener(sumberACListener);
        }
        return sumberAC;
    }

    public MutableLiveData<String> getKwh() {
        if (kwh == null) {
            kwh = new MutableLiveData<>();
            // read data kwh dari firebase
            kwhListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai kwh dari firebase ke tipe data String
                    String nilaiKwh = snapshot.getValue(String.class);
                    // kasih nilaiKwh ke kwh untuk dikirim ke SmartLightFragment
                    kwh.setValue(nilaiKwh);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data kwh dari firebase", error.toException());
                }
            };
            kwhRef.addValueEventListener(kwhListener);
        }
        return kwh;
    }

    public MutableLiveData<Boolean> getMode() {
        if (mode == null) {
            mode = new MutableLiveData<>();
            // read data mode dari firebase
            modeListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai mode dari firebase ke tipe data String
                    String nilaiMode = snapshot.getValue(String.class);
                    // perikasi nilaiMode untuk menyesuaikan apakah mode otomatis / manual

                    // manual
                    if (nilaiMode.equalsIgnoreCase("Manual")) {
                        mode.setValue(false);
                    }
                    // otomatis
                    else if (nilaiMode.equalsIgnoreCase("Otomatis")) {
                        mode.setValue(true);
                    }
                    // default nya otomatis
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data mode dari firebase", error.toException());
                }
            };
            modeGetterRef.addValueEventListener(modeListener);
        }
        return mode;
    }

    public Task<Void> updateLamp1(String state) {
        return lamp1SetterRef.setValue(state);
    }

    public Task<Void> updateLamp2(String state) {
        return  lamp2SetterRef.setValue(state);
    }

    public Task<Void> updateMode(boolean newMode){
        String stringMode = "";
        if (newMode == true){
            stringMode = "1";
        } else {
            stringMode = "0";
        }

        return modeSetterRef.setValue(stringMode);
    }
}