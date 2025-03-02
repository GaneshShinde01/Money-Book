package com.gs.moneybook.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentEditProfileBinding;
import com.gs.moneybook.databinding.FragmentViewProfileBinding;

public class ViewProfileFragment extends Fragment {
    FragmentViewProfileBinding binding;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_view_profile, container, false);
        binding = FragmentViewProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();



        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}