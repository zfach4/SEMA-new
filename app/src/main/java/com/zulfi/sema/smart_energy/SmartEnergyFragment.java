package com.zulfi.sema.smart_energy;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.zulfi.sema.R;

public class SmartEnergyFragment extends Fragment {

    private SmartEnergyViewModel mViewModel;

    private Switch switchMode;
    private TextView tvMode;
    private View manualContentView;
    private View autoContentView;

    public static SmartEnergyFragment newInstance() {
        return new SmartEnergyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_energy, container, false);

        manualContentView = view.findViewById(R.id.content_manual);
        autoContentView = view.findViewById(R.id.content_auto);

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
        // TODO: Use the ViewModel
    }

    private void setContentMode(Boolean isChecked) {
        if (isChecked) { // otomatis
            tvMode.setText(R.string.auto_title);
            manualContentView.setVisibility(View.GONE);
            autoContentView.setVisibility(View.VISIBLE);
        } else { // manual
            tvMode.setText(R.string.manual_title);
            manualContentView.setVisibility(View.VISIBLE);
            autoContentView.setVisibility(View.GONE);
        }
    }

}