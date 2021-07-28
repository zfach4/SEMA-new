package com.zulfi.sema.smart_energy;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.zulfi.sema.R;

public class SmartEnergyFragment extends Fragment {

    private SmartEnergyViewModel mViewModel;

    private Switch switchMode;

    private TextView tvMode;
    private View manualContentView;
    private View autoContentView;

    // auto content
    private ProgressBar chartBatteryPercentage;
    private TextView tvBatteryPercentage;

    // manual content
    private TextView tvServoAngle;
    private double servoAngle = 0;
    private Button btnUpdateServo;
    private EditText etServo;
    private TextView tvConfirm;

    // table content
    private TextView tvAccuVoltage;
    private TextView tvSolarPanelVoltage;
    private TextView tvSolarPanelCurrent;
    private TextView tvAzimuth;

    private TableRow rowSudutServo;
    private TextView tvSudutServoTable;

    public static SmartEnergyFragment newInstance() {
        return new SmartEnergyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_energy, container, false);

        // automate content
        autoContentView = view.findViewById(R.id.content_auto);
        // battery percentage in pie chart
        View pieContentView = autoContentView.findViewById(R.id.chart_battery);
        chartBatteryPercentage = pieContentView.findViewById(R.id.stats_progressbar);
        tvBatteryPercentage = pieContentView.findViewById(R.id.tv_battery_percentage);
        // accu and solar panel measure
        View tableContentView = view.findViewById(R.id.contont_table);
        tvAccuVoltage = tableContentView.findViewById(R.id.tv_accu_voltage);
        tvSolarPanelVoltage = tableContentView.findViewById(R.id.tv_solar_voltage);
        tvSolarPanelCurrent = tableContentView.findViewById(R.id.tv_solar_current);
        // azimuth
        tvAzimuth = tableContentView.findViewById(R.id.tv_azimuth);
        // sudut servo (tulisan kecil, paling bawah)
        rowSudutServo = tableContentView.findViewById(R.id.row_sudut_servo);
        tvSudutServoTable = tableContentView.findViewById(R.id.tv_servo);

        // manual content
        manualContentView = view.findViewById(R.id.content_manual);
        tvServoAngle = manualContentView.findViewById(R.id.tv_servo_angle);
        tvConfirm = manualContentView.findViewById(R.id.tv_confirm);
        etServo = manualContentView.findViewById(R.id.et_sudut_servo);
        btnUpdateServo = manualContentView.findViewById(R.id.btn_update_servo);

//        Button btnCW = manualContentView.findViewById(R.id.btn_cw);
//        btnCW.setOnClickListener(v -> {
//            changeServoAngleClockwise();
//        });
//
//        Button btnCCW = manualContentView.findViewById(R.id.btn_ccw);
//        btnCCW.setOnClickListener(v -> {
//            changeServoAngleCounterClockwise();
//        });

        tvMode = view.findViewById(R.id.tv_mode_state);
        switchMode = view.findViewById(R.id.switch_mode);
        // dipanggil kalau user mencet switch
        switchMode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switchMode.setTag(null);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SmartEnergyViewModel.class);

        // get data2 dari ViewModel
        loadDataFromFirebase();

        // tambah listener untuk handle success / failure saat write data ke firebase
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // check apakah ini dari user
                if (buttonView.getTag() != null) {
                    buttonView.setTag(null);
                    return;
                }

                Log.d("Zulfi Fachrurrozi", "mode sekarang : " + isChecked );
                // kirim data ke firebase
                mViewModel.updateMode(isChecked).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Berhasil Mengubah Mode", Toast.LENGTH_SHORT).show();
                        Log.d("Update Mode", "Berhasil update mode ke " + isChecked);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Update Mode", "Gagal update mode ke " + isChecked);
                        switchMode.setChecked(!isChecked);
                    }
                });
            }
        });

        btnUpdateServo.setOnClickListener(v -> {
            //mengupdate sudut servo
            String oldConfirm = tvConfirm.getText().toString();
            String newServo = etServo.getText().toString();
            tvConfirm.setText(R.string.data_confirm);
            Log.d("Zulfi Fachrurrozi", "Mengirim ke Firebase nilai Sudut Servo : " + newServo);
            mViewModel.updateSudutServo(newServo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Update Sudut Servo", "Gagal memperbaharui sudut Servo");
                    tvConfirm.setText(oldConfirm);
                }
            });
            //mengupdate confirm text
            mViewModel.updateConfirm().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Update Confirm", "Gagal memperbaharui Confirm");
                }
            });
        });

    }

    private void setContentMode(Boolean isChecked) {
        // harusnya ambil data dulu dari server untuk angka2nya
        if (!isChecked) { // manual
            tvMode.setText(R.string.manual_title);
            autoContentView.setVisibility(View.GONE);
            manualContentView.setVisibility(View.VISIBLE);
            rowSudutServo.setVisibility(View.GONE);

        } else { // otomatis
            tvMode.setText(R.string.auto_title);
            autoContentView.setVisibility(View.VISIBLE);
            manualContentView.setVisibility(View.GONE);
            rowSudutServo.setVisibility(View.VISIBLE);
        }
    }

    private void changeServoAngleClockwise() {
        if (servoAngle > 0) {
            servoAngle -= 1;
        }

        tvServoAngle.setText(servoAngle + "");
    }

    private void changeServoAngleCounterClockwise() {
        if (servoAngle < 180) {
            servoAngle +=1;
        }
        tvServoAngle.setText(servoAngle + "");
    }

    // load data dari viewmodel
    private void loadDataFromFirebase() {
        // mode
        mViewModel.getMode().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                switchMode.setTag("SET_BY_APP");
                switchMode.setChecked(aBoolean);
                setContentMode(aBoolean);
            }
        });

        // tegangan accu
        mViewModel.getVAki().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvAccuVoltage.setText(s);
                int batteryPercentage = (int) Math.round((Double.parseDouble(s) * 100) / 12);
                chartBatteryPercentage.setProgress(batteryPercentage);
                tvBatteryPercentage.setText(batteryPercentage + "%");
            }
        });

        // tegangan panel surya
        mViewModel.getVPanel().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvSolarPanelVoltage.setText(s);
            }
        });

        // arus panel surya
        mViewModel.getAPanel().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvSolarPanelCurrent.setText(s);
            }
        });

        // azimuth
        mViewModel.getAzimuth().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvAzimuth.setText(s);
            }
        });

        mViewModel.getPosisiServo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                servoAngle = Double.parseDouble(s);
                tvServoAngle.setText(s);
                tvSudutServoTable.setText(s);
            }
        });

        mViewModel.getConfirmText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvConfirm.setText(s);
            }
        });
    }
}