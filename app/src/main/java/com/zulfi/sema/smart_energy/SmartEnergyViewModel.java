package com.zulfi.sema.smart_energy;

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

public class SmartEnergyViewModel extends ViewModel {

    // variable realtime - dapatkan value
    private MutableLiveData<String> vAki; // tegangan accu
    private MutableLiveData<String> vPanel; // tegangan panel surya
    private MutableLiveData<String> aPanel; // arus panel surya
    private MutableLiveData<String> azimuth;
    private MutableLiveData<String> posisiServo;

    private MutableLiveData<Boolean> mode;
    private MutableLiveData<String> confirmText;
    private MutableLiveData<String> hour;
    private MutableLiveData<String> minute;

    // alamat database untuk tiap data item di firebase
    private DatabaseReference vAkiRef;
    private DatabaseReference vPanelRef;
    private DatabaseReference aPanelRef;
    private DatabaseReference azimuthRef;
    private DatabaseReference posisiServoGetterRef;
    private DatabaseReference posisiServoSetterRef;
    private DatabaseReference modeGetterRef;
    private DatabaseReference modeSetterRef;
    private DatabaseReference confirmRef;

    private DatabaseReference hourRef;
    private DatabaseReference minuteRef;


    // listener
    private ValueEventListener vAkiListener;
    private ValueEventListener vPanelListener;
    private ValueEventListener aPanelListener;
    private ValueEventListener azimuthListener;
    private ValueEventListener posisiServoListener;

    private ValueEventListener modeListener;
    private ValueEventListener confirmListener;

    private ValueEventListener hourListener;
    private ValueEventListener minuteListener;

    public SmartEnergyViewModel() {
        // Sensor
        DatabaseReference sensorRef = FirebaseDatabase.getInstance().getReference("DataUpdateTerakhir");
        // Sensor children: Aki, vPanel, aPanel, Azimuth, PosisiServo
        vAkiRef = sensorRef.child("VoltageBaterai");
        vPanelRef = sensorRef.child("VoltagePanelSUrya");
        aPanelRef = sensorRef.child("CurrentPanelSurya");
        azimuthRef = sensorRef.child("NilaiSudutAzimuthMathari");
        modeGetterRef = sensorRef.child("Mode");
        posisiServoGetterRef = sensorRef.child("NilaiSudutServo");
        hourRef = sensorRef.child("Jam");
        minuteRef = sensorRef.child("Menit");

        // Control
        DatabaseReference controlRef = FirebaseDatabase.getInstance().getReference("Control");
        // SmartCityAE18 children: Mode
        modeSetterRef = controlRef.child("Mode");
        confirmRef = controlRef.child("Confirm");
        posisiServoSetterRef = controlRef.child("Servo");

    }

    // yang harus dilakukan ketika class ini akan dihapus
    // untuk bersih-bersih agar sebagian proses yang tidak dipakai tidak membebani aplikasi
    @Override
    protected void onCleared() {
        // hapus listener bila sudah di-create

        if (vAkiListener != null) {
            vAkiRef.removeEventListener(vAkiListener);
        }

        if (vPanelListener != null) {
            vPanelRef.removeEventListener(vPanelListener);
        }

        if (aPanelListener != null) {
            aPanelRef.removeEventListener(aPanelListener);
        }

        if (azimuthListener != null) {
            azimuthRef.removeEventListener(azimuthListener);
        }

        if (posisiServoListener != null) {
            posisiServoGetterRef.removeEventListener(posisiServoListener);
        }

        if (modeListener != null) {
            modeGetterRef.removeEventListener(modeListener);
        }

        if (confirmListener != null) {
            confirmRef.removeEventListener(confirmListener);
        }

        if (hourListener != null) {
            hourRef.removeEventListener(hourListener);
        }

        if (minuteListener != null) {
            minuteRef.removeEventListener(minuteListener);
        }
    }

    public MutableLiveData<String> getVAki() {
        if (vAki == null) {
            vAki = new MutableLiveData<>();
            // read data aki dari firebase
            vAkiListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai Aki dari firebase ke tipe data String
                    String nilaiVAki = snapshot.getValue(String.class);
                    // kasih nilaiVAki ke vAki untuk dikirim ke SmartEnergyFragment
                    vAki.setValue(nilaiVAki);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data Aki dari firebase", error.toException());
                }
            };
            vAkiRef.addValueEventListener(vAkiListener);
        }
        return vAki;
    }

    public MutableLiveData<String> getVPanel() {
        if (vPanel == null) {
            vPanel = new MutableLiveData<>();
            // read data vPanel dari firebase
            vPanelListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai vPanel dari firebase ke tipe data String
                    String nilaiVPanel = snapshot.getValue(String.class);
                    // kasih nilaiVPanel ke vPanel untuk dikirim ke SmartEnergyFragment
                    vPanel.setValue(nilaiVPanel);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data vPanel dari firebase", error.toException());
                }
            };
            vPanelRef.addValueEventListener(vPanelListener);
        }
        return vPanel;
    }

    public MutableLiveData<String> getAPanel() {
        if (aPanel == null) {
            aPanel = new MutableLiveData<>();
            // read data aPanel dari firebase
            aPanelListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai aPanel dari firebase ke tipe data String
                    String nilaiAPanel = snapshot.getValue(String.class);
                    // kasih nilaiAPanel ke aPanel untuk dikirim ke SmartEnergyFragment
                    aPanel.setValue(nilaiAPanel);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data aPanel dari firebase", error.toException());
                }
            };
            aPanelRef.addValueEventListener(aPanelListener);
        }
        return aPanel;
    }

    public MutableLiveData<String> getAzimuth() {
        if (azimuth == null) {
            azimuth = new MutableLiveData<>();
            // read data Azimuth dari firebase
            azimuthListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai Azimuth dari firebase ke tipe data String
                    String nilaiAzimuth = snapshot.getValue(String.class);
                    // kasih nilaiAzimuth ke azimuth untuk dikirim ke SmartEnergyFragment
                    azimuth.setValue(nilaiAzimuth);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data azimuth dari firebase", error.toException());
                }
            };
            azimuthRef.addValueEventListener(azimuthListener);
        }
        return azimuth;
    }

    public MutableLiveData<String> getPosisiServo() {
        if (posisiServo == null) {
            posisiServo = new MutableLiveData<>();
            // read data PosisiServo dari firebase
            posisiServoListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai PosisiServo dari firebase ke tipe data String
                    String nilaiPosisiServo = snapshot.getValue(String.class);
                    // kasih nilaiPosisiServo ke posisiServo untuk dikirim ke SmartEnergyFragment
                    posisiServo.setValue(nilaiPosisiServo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data PosisiServo dari firebase", error.toException());
                }
            };
            posisiServoGetterRef.addValueEventListener(posisiServoListener);
        }
        return posisiServo;
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

    public MutableLiveData<String> getConfirmText() {
        if (confirmText == null) {
            confirmText = new MutableLiveData<>();
            // read data confirmText dari firebase
            confirmListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai Confirm dari firebase ke tipe data String
                    String nilaiConfirmText = snapshot.getValue(String.class);
                    // kasih nilaiConfirmText ke confirmText untuk dikirim ke SmartEnergyFragment
                    confirmText.setValue(nilaiConfirmText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data confirmText dari firebase", error.toException());
                }
            };
            confirmRef.addValueEventListener(confirmListener);
        }
        return confirmText;
    }

    public MutableLiveData<String> getHour() {
        if (hour == null) {
            hour = new MutableLiveData<>();
            // read data hour dari firebase
            hourListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai hour dari firebase ke tipe data String
                    String nilaiHour = snapshot.getValue(String.class);
                    // kasih nilaiHour ke hour untuk dikirim ke SmartEnergyFragment
                    hour.setValue(nilaiHour);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data hour dari firebase", error.toException());
                }
            };
            hourRef.addValueEventListener(hourListener);
        }
        return hour;
    }

    public MutableLiveData<String> getMinute() {
        if (minute == null) {
            minute = new MutableLiveData<>();
            // read data minute dari firebase
            minuteListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // parsing nilai minute dari firebase ke tipe data String
                    String nilaiMinute = snapshot.getValue(String.class);
                    // kasih nilaiMinute ke minute untuk dikirim ke SmartEnergyFragment
                    minute.setValue(nilaiMinute);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Zulfi Firebase", "Gagal read data minute dari firebase", error.toException());
                }
            };
            minuteRef.addValueEventListener(minuteListener);
        }
        return minute;
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

    public Task<Void> updateSudutServo(String newSudutServo){
        return posisiServoSetterRef.setValue(newSudutServo);
    }

    public Task<Void> updateConfirm(){
        return confirmRef.setValue("Data belum sampai ke NodeMCU");
    }
}