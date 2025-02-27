package com.gs.moneybook.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gs.moneybook.Adapters.DemoData;
import com.gs.moneybook.Adapters.TransactionAdapter;
import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.Model.TransactionModel;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentAllTransactionsBinding;

import java.util.ArrayList;
import java.util.List;

public class AllTransactionsFragment extends Fragment {

    private FragmentAllTransactionsBinding binding;
    private DBHelper dbHelper;
    private TransactionAdapter transactionAdapter;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1
    private List<TransactionModel> transactionList;




    public AllTransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_all_transactions, container, false);
        binding = FragmentAllTransactionsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.transactionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dbHelper = DBHelper.getInstance(requireContext());

        // Fetch transactions from database
        List<TransactionModel> transactionList = dbHelper.getTransactions(loggedInUserId);

        Log.d("AllTransationFragment","Transaction list size: "+transactionList.size());

        // Get demo transaction data
       // transactionList = DemoData.getDemoTransactions();
        // Set the adapter
        transactionAdapter = new TransactionAdapter(requireContext(), transactionList);
        binding.transactionRecyclerView.setAdapter(transactionAdapter);

        //transactionAdapter.notifyDataSetChanged();


//        ArrayAdapter<TransactionModel> adapter = new ArrayAdapter<>(requireContext(),R.layout.listview_item,transactionList);
//        binding.lialltransactions.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}