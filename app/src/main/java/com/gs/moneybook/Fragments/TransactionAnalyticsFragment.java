package com.gs.moneybook.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentTransactionAnalyticsBinding;

public class TransactionAnalyticsFragment extends Fragment {
    private FragmentTransactionAnalyticsBinding binding;

    public TransactionAnalyticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_transaction_analytics, container, false);
        binding = FragmentTransactionAnalyticsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        return view;
    }
}