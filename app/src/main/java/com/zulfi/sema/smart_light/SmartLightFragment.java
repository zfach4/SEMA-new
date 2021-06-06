package com.zulfi.sema.smart_light;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zulfi.sema.R;

public class SmartLightFragment extends Fragment {

    private SmartLightViewModel mViewModel;

    private ImageButton btnLight;
    private TextView tvLightState;

    private Boolean isLightOn;

    public static SmartLightFragment newInstance() {
        return new SmartLightFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_light, container, false);

        btnLight = view.findViewById(R.id.btn_light);
        btnLight.setOnClickListener(v -> {
            btnLightClicked();
        });

        tvLightState = view.findViewById(R.id.tv_light_state);

        // dummy -- harusnya ngambil state dari server
        isLightOn = false;

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SmartLightViewModel.class);
        // TODO: Use the ViewModel
    }

    private void btnLightClicked() {
        // harusnya sebelum di-set kebalikannya, dia kirim data state ke server dulu
        this.isLightOn = !this.isLightOn;
        setLightState();
    }

    private void setLightState() {
        if (isLightOn) {
            btnLight.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on));
            tvLightState.setText(R.string.light_on_title);
        } else {
            btnLight.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off));
            tvLightState.setText(R.string.light_off_title);
        }
    }

}