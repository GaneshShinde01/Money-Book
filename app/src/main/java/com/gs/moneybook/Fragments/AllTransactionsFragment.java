package com.gs.moneybook.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gs.moneybook.Adapters.TransactionAdapter;
import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.Model.TransactionModel;
import com.gs.moneybook.databinding.FragmentAllTransactionsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllTransactionsFragment extends Fragment {

    private FragmentAllTransactionsBinding binding;
    private DBHelper dbHelper;
    private TransactionAdapter transactionAdapter;
    private int loggedInUserId = 1;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AllTransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllTransactionsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.transactionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dbHelper = DBHelper.getInstance(requireContext());

        // Initialize adapter with an empty list
        transactionAdapter = new TransactionAdapter(requireContext(), new ArrayList<>());
        binding.transactionRecyclerView.setAdapter(transactionAdapter);


        executorService.execute(() -> {
            List<TransactionModel> transactions = dbHelper.getTransactions(loggedInUserId);
            Log.d("AllTransationFragment", "Transaction list size: " + transactions.size());

            for(TransactionModel transactionModel : transactions){
                Log.d("AllTransationFragment", "Transaction: " + transactions.toString()); // Or log specific fields

            }

            requireActivity().runOnUiThread(() -> {
                if (transactions.isEmpty()) {
                    binding.emptyTransactionsTextView.setVisibility(View.VISIBLE);
                    binding.transactionRecyclerView.setVisibility(View.GONE);
                } else {
                    binding.emptyTransactionsTextView.setVisibility(View.GONE);
                    binding.transactionRecyclerView.setVisibility(View.VISIBLE);
                    transactionAdapter.updateData(transactions); //use update data method from Adapter

                }
            });
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        executorService.shutdown();
    }

    @Override
    public void onResume() {
        super.onResume();
        //refreshTransactions();
    }

   /* private void refreshTransactions() {
        executorService.execute(() -> {
            List<TransactionModel> transactions = dbHelper.getTransactions();
            Log.d("AllTransationFragment", "Transaction list size: " + transactions.size());
            requireActivity().runOnUiThread(() -> {
                if (transactions.isEmpty()) {
                    binding.emptyTransactionsTextView.setVisibility(View.VISIBLE);
                    binding.transactionRecyclerView.setVisibility(View.GONE);
                } else {
                    binding.emptyTransactionsTextView.setVisibility(View.GONE);
                    binding.transactionRecyclerView.setVisibility(View.VISIBLE);
                    transactionAdapter = new TransactionAdapter(requireContext(), transactions);
                    binding.transactionRecyclerView.setAdapter(transactionAdapter);
                }
            });
        });
    }*/
}