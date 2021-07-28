package com.zulfi.sema.smart_light;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SmartLightViewModel.class);

        loadDataFromFirebase();

        btnLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newState;
                isLightOn1 = !isLightOn1;
                if (isLightOn1){
                    newState = "ON";
                } else {
                    newState = "OFF";
                }
                mViewModel.updateLamp1(newState).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Update Lamp1", "Gagal memperbaharui Lamp1 menjadi " + newState);
                        isLightOn1 = !isLightOn1;
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
                    newState = "ON";
                } else {
                    newState = "OFF";
                }
                mViewModel.updateLamp2(newState).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Update Lamp2", "Gagal memperbaharui Lamp2 menjadi " + newState);
                        isLightOn2 = !isLightOn2;
                    }
                });
            }
        });
    }

    private void loadDataFromFirebase(){
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