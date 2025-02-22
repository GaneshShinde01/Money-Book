package com.gs.moneybook.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentAddExpenseBinding;

import java.util.ArrayList;

public class AddExpenseFragment extends Fragment {

    FragmentAddExpenseBinding binding;
    private ArrayList<String> expenseCategories;
    private ArrayList<String> paymentModes;
    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> paymentModeAdapter;



    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false);

        // Initialize categories and payment modes
        expenseCategories = new ArrayList<>();
        expenseCategories.add("Food");
        expenseCategories.add("Transport");
        expenseCategories.add("Entertainment");
        expenseCategories.add("Shopping");
        expenseCategories.add("Rent");

        paymentModes = new ArrayList<>();
        paymentModes.add("Cash");
        paymentModes.add("Credit Card");
        paymentModes.add("Debit Card");
        paymentModes.add("Net Banking");
        paymentModes.add("UPI");

        // Set up category spinner adapter
        categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, expenseCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.expenseCategorySpinner.setAdapter(categoryAdapter);

        // Set up payment mode spinner adapter
        paymentModeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, paymentModes);
        paymentModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.paymentModeSpinner.setAdapter(paymentModeAdapter);

        // Handle add expense button click
        binding.addExpenseButton.setOnClickListener(v -> {
            String amount = binding.expenseAmountEditText.getText().toString().trim();
            String category = binding.expenseCategorySpinner.getSelectedItem().toString();
            String paymentMode = binding.paymentModeSpinner.getSelectedItem().toString();
            String description = binding.expenseDescriptionEditText.getText().toString().trim();

            if (amount.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
            } else {
                // Add expense to database or handle it as needed
                Toast.makeText(getActivity(), "Expense added: " + amount + " " + category, Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }
}