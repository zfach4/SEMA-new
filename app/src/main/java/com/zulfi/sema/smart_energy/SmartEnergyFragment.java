package com.zulfi.sema.smart_energy;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

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
    private TextView tvAccuVoltage;
    private TextView tvSolarPanelVoltage;
    private TextView tvSolarPanelCurrent;
    private TextView tvAzimuth;
    private TextView tvSudutServoAutoContent;

    // manual content
    private TextView tvServoAngle;
    private int servoAngle = 177;

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
        tvAccuVoltage = autoContentView.findViewById(R.id.tv_accu_voltage);
        tvSolarPanelVoltage = autoContentView.findViewById(R.id.tv_solar_voltage);
        tvSolarPanelCurrent = autoContentView.findViewById(R.id.tv_solar_current);
        // azimuth
        tvAzimuth = autoContentView.findViewById(R.id.tv_azimuth);
        // sudut servo (tulisan kecil, paling bawah)
        tvSudutServoAutoContent = autoContentView.findViewById(R.id.tv_servo);

        // manual content
        manualContentView = view.findViewById(R.id.content_manual);
        tvServoAngle = manualContentView.findViewById(R.id.tv_servo_angle);

        Button btnCW = manualContentView.findViewById(R.id.btn_cw);
        btnCW.setOnClickListener(v -> {
            changeServoAngleClockwise();
        });

        Button btnCCW = manualContentView.findViewById(R.id.btn_ccw);
        btnCCW.setOnClickListener(v -> {
            changeServoAngleCounterClockwise();
        });


        tvMode = view.findViewById(R.id.tv_mode_state);
        switchMode = view.findViewById(R.id.switch_mode);
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> setContentMode(isChecked));

        // set initial state
        // harusnya ngambil data dari server
        setContentMode(switchMode.isChecked());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SmartEnergyViewModel.class);

        // get data2 dari ViewModel
        loadDataFromFirebase();
    }

    private void setContentMode(Boolean isChecked) {
        // harusnya ambil data dulu dari server untuk angka2nya
        if (!isChecked) { // manual
            tvMode.setText(R.string.manual_title);
            autoContentView.setVisibility(View.GONE);
            manualContentView.setVisibility(View.VISIBLE);

        } else { // otomatis
            tvMode.setText(R.string.auto_title);
            autoContentView.setVisibility(View.VISIBLE);
            manualContentView.setVisibility(View.GONE);

            // battery percentage in pie chart
            // data bohongan
            // harusnya ngambil data dari server
            int batteryPercentage = 55;
            chartBatteryPercentage.setProgress(batteryPercentage);
            tvBatteryPercentage.setText(batteryPercentage + "%");
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
                switchMode.setChecked(aBoolean);
                setContentMode(aBoolean);
            }
        });

        // tegangan accu
        mViewModel.getVAki().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvAccuVoltage.setText(s);
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
                tvServoAngle.setText(s);
                tvSudutServoAutoContent.setText(s);
            }
        });
    }
}