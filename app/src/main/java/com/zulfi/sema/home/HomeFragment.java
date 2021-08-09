package com.zulfi.sema.home;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zulfi.sema.MainActivity;
import com.zulfi.sema.R;
import com.zulfi.sema.smart_energy.SmartEnergyFragment;
import com.zulfi.sema.smart_light.SmartLightFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity act = (MainActivity) getActivity();


        // tambah handler untuk btn_menu_smart_energy
        Button btnSmartEnergy = view.findViewById(R.id.btn_menu_smart_energy);
        btnSmartEnergy.setOnClickListener(v -> {
            changeFragment(new SmartEnergyFragment());
            act.navigationView.setCheckedItem(R.id.nav_smart_energy);
        });

        // tambah handler untuk btn_menu_smart_light
        Button btnSmartLight = view.findViewById(R.id.btn_menu_smart_light);
        btnSmartLight.setOnClickListener(v -> {
            changeFragment(new SmartLightFragment());
            act.navigationView.setCheckedItem(R.id.nav_smart_light);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    private void changeFragment(Fragment fragment) {
        MainActivity activity = (MainActivity) getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }

}