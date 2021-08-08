package com.zulfi.sema.smart_light;

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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.zulfi.sema.R;

public class SmartLightFragment extends Fragment {

    private SmartLightViewModel mViewModel;

    private ImageButton btnLight1, btnLight2;
    private TextView tvLightState1, tvLightState2, tvSumberAC, tvKwh;

    private Boolean isLightOn1 = false, isLightOn2 = false;

    private ProgressBar progress;
    private View contentTable;

    private View contentViewLamp1;
    private View contentViewLamp2;

    private Switch switchMode;
    private TextView tvMode;

    public static SmartLightFragment newInstance() {
        return new SmartLightFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_light, container, false);

        btnLight1 = view.findViewById(R.id.btn_light1);
        btnLight2 = view.findViewById(R.id.btn_light2);

        tvLightState1 = view.findViewById(R.id.tv_light_state1);
        tvLightState2 = view.findViewById(R.id.tv_light_state2);

        tvSumberAC = view.findViewById(R.id.tv_sumber_listrik_ac);
        tvKwh = view.findViewById(R.id.tv_kwh);
        progress = view.findViewById(R.id.pb_smart_light);
        contentTable = view.findViewById(R.id.content_table_smart_light);
        contentViewLamp1 = view.findViewById(R.id.content_view_lamp1);
        contentViewLamp2 = view.findViewById(R.id.content_view_lamp2);

        View contentMode = view.findViewById(R.id.content_switch);
        tvMode = contentMode.findViewById(R.id.tv_mode_state);
        switchMode = contentMode.findViewById(R.id.switch_mode);
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
        mViewModel = new ViewModelProvider(this).get(SmartLightViewModel.class);

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
                progress.setVisibility(View.VISIBLE);
                contentViewLamp1.setVisibility(View.GONE);
                contentViewLamp2.setVisibility(View.GONE);
                switchMode.setEnabled(false);
                tvMode.setText("-");
                mViewModel.updateMode(isChecked).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Mengubah Mode", Toast.LENGTH_SHORT).show();
                        Log.d("Update Mode", "Update mode ke " + isChecked);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Update Mode", "Gagal update mode ke " + isChecked);
                        switchMode.setChecked(!isChecked);
                        setContentMode(switchMode.isChecked());
                        switchMode.setEnabled(true);
                        contentViewLamp1.setVisibility(View.VISIBLE);
                        contentViewLamp2.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        btnLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newState;
                isLightOn1 = !isLightOn1;
                if (isLightOn1){
                    newState = "1";
                } else {
                    newState = "0";
                }
                btnLight1.setEnabled(false);
                mViewModel.updateLamp1(newState).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Memperbaharui Data Lamp1", Toast.LENGTH_SHORT).show();
                        Log.d("Update Status", "Lamp1 =  " + newState);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Update Lamp1", "Gagal memperbaharui Lamp1 menjadi " + newState);
                        isLightOn1 = !isLightOn1;
                        btnLight1.setEnabled(true);
                    }
                });
            }
        });

        btnLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newState;
                isLightOn2 = !isLightOn2;
                if (isLightOn2){
                    newState = "1";
                } else {
                    newState = "0";
                }
                btnLight2.setEnabled(false);
                mViewModel.updateLamp2(newState).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Memperbaharui Data Lamp2", Toast.LENGTH_SHORT).show();
                        Log.d("Update Status", "Lamp2 =  " + newState);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Update Lamp2", "Gagal memperbaharui Lamp2 menjadi " + newState);
                        isLightOn2 = !isLightOn2;
                        btnLight2.setEnabled(true);
                    }
                });
            }
        });
    }

    private void setContentMode(boolean checked) {
        if (checked){
            tvMode.setText("Otomatis");
            btnLight1.setEnabled(false);
            btnLight2.setEnabled(false);
        } else {
            tvMode.setText("Manual");
            btnLight1.setEnabled(true);
            btnLight2.setEnabled(true);
        }
    }

    private void loadDataFromFirebase(){
        progress.setVisibility(View.VISIBLE);
        // mode
        mViewModel.getMode().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                switchMode.setTag("SET_BY_APP");
                switchMode.setChecked(aBoolean);
                // disable atau enable button lamp
                setContentMode(aBoolean);
                switchMode.setEnabled(true);

                contentTable.setVisibility(View.VISIBLE);
                contentViewLamp1.setVisibility(View.VISIBLE);
                contentViewLamp2.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                contentViewLamp1.setVisibility(View.VISIBLE);
                contentViewLamp2.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getLamp1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equalsIgnoreCase("ON")){
                    btnLight1.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    tvLightState1.setText(R.string.light_on_title1);
                    isLightOn1 = true;

                } else {
                    btnLight1.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    tvLightState1.setText(R.string.light_off_title1);
                    isLightOn1 = false;
                }
                btnLight1.setEnabled(true);
            }
        });

        mViewModel.getLamp2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equalsIgnoreCase("ON")){
                    btnLight2.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
                    tvLightState2.setText(R.string.light_on_title2);
                    isLightOn2 = true;

                } else {
                    btnLight2.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
                    tvLightState2.setText(R.string.light_off_title2);
                    isLightOn2 = false;
                }
                btnLight2.setEnabled(true);
            }
        });

        mViewModel.getSumberAC().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.isEmpty()){
                    tvSumberAC.setText("Inverter");
                } else {
                    tvSumberAC.setText(s);
                }
            }
        });

        mViewModel.getKwh().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvKwh.setText(s);
            }
        });

    }
}