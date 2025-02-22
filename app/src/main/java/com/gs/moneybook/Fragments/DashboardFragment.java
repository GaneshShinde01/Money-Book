package com.gs.moneybook.Fragments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        // Open drawer when button is clicked
        binding.openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = getActivity().findViewById(com.gs.moneybook.R.id.drawer_layout);
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(getActivity().findViewById(com.gs.moneybook.R.id.navigation_view));
                }
            }
        });

        return binding.getRoot();

    }
}