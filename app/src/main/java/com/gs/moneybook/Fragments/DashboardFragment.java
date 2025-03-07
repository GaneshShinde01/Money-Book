package com.gs.moneybook.Fragments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gs.moneybook.Adapters.InvestmentAdapter;
import com.gs.moneybook.Adapters.TransactionAdapter;
import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.Model.InvestmentModel;
import com.gs.moneybook.Model.TransactionModel;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentDashboardBinding;

import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming you have a logged-in user ID
    private TransactionAdapter transactionAdapter;
    private double totalSavings = 0.0f;
    private double totalExpense = 0.0f;



    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        dbHelper = DBHelper.getInstance(getContext());


       /* // Open drawer when button is clicked
        binding.openDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = getActivity().findViewById(com.gs.moneybook.R.id.drawer_layout);
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(getActivity().findViewById(com.gs.moneybook.R.id.navigation_view));
                }
            }
        });
*/
        totalSavings = dbHelper.getSavingsAmountForDashboard(loggedInUserId);
        totalExpense = dbHelper.getTotalExpenseForDashboard(loggedInUserId);

        binding.tvTotalSavingsDashboard.setText(String.format("%.2f", totalSavings));
        binding.tvTotalExpensesDashboard.setText(String.format("%.2f", totalExpense));


        loadRecyclerView();



        return binding.getRoot();

    }

    public void loadRecyclerView(){

        List<TransactionModel> transactionModels = dbHelper.getTransactions(loggedInUserId);

        // Check if the list is empty before setting up the adapter
        if (transactionModels != null && !transactionModels.isEmpty()) {

            Collections.reverse(transactionModels); //used to reverse the list

//            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
//            layoutManager.setReverseLayout(true);
//            layoutManager.setStackFromEnd(true);
//            // Set up the RecyclerView with LinearLayoutManager
//            binding.recyclerViewTransactionsDashboard.setLayoutManager(layoutManager);

            binding.recyclerViewTransactionsDashboard.setLayoutManager(new LinearLayoutManager(requireContext()));

            // Initialize the adapter with the list of investments
            transactionAdapter = new TransactionAdapter(requireContext(), transactionModels);

            // Set the adapter to the RecyclerView
            binding.recyclerViewTransactionsDashboard.setAdapter(transactionAdapter);
        } else {
            // Handle the case when no investments are available
            Toast.makeText(requireContext(), "No investment history available.", Toast.LENGTH_SHORT).show();
        }
    }
}