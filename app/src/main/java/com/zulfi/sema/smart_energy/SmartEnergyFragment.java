package com.zulfi.sema.smart_energy;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zulfi.sema.R;

public class SmartEnergyFragment extends Fragment {

    private SmartEnergyViewModel mViewModel;

    public static SmartEnergyFragment newInstance() {
        return new SmartEnergyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_smart_energy, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SmartEnergyViewModel.class);
        // TODO: Use the ViewModel
    }

}