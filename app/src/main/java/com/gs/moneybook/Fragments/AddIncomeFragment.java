package com.gs.moneybook.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.Model.TransactionModel;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentAddIncomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddIncomeFragment extends Fragment {

    private FragmentAddIncomeBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1
    private final String ADD_NEW_CATEGORY = "Add New Category";

    public AddIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddIncomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = new DBHelper(requireContext());

        // Load categories for the logged-in user from the database
        loadCategories();

        // Add Income button click listener
        binding.addIncomeButton.setOnClickListener(v -> {
            if (validateInput()) {
                // Add income to the database
                addIncome();
            }
        });

        // Listen for category selection in AutoCompleteTextView
        binding.autoCompleteEditText.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCategory = (String) parent.getItemAtPosition(position);
            if (selectedCategory.equals(ADD_NEW_CATEGORY)) {
                // Redirect to AddCategoryFragment
                navigateToAddCategoryFragment();
            }
        });

        return view;
    }

    // Method to load income categories from the database
    private void loadCategories() {
        List<String> categoryNames = new ArrayList<>();
        List<String> categoryList = dbHelper.getCategoriesByTypeAndUserId("Income", loggedInUserId);

        categoryNames.addAll(categoryList);
        categoryNames.add(ADD_NEW_CATEGORY); // Add "Add New Category" option

        // Set up the adapter for AutoCompleteTextView (suggestions dropdown)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                categoryNames
        );
        binding.autoCompleteEditText.setAdapter(adapter);
    }

    // Method to add income
    private void addIncome() {
        // Get user inputs
        String categoryName = binding.autoCompleteEditText.getText().toString();
        String amountString = binding.incomeAmountEditText.getText().toString();

        if (!TextUtils.isEmpty(amountString)) {
            double amount = Double.parseDouble(amountString);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Fetch the category ID based on the category name and logged-in user ID
            int categoryId = dbHelper.getCategoryIdByName(categoryName, loggedInUserId);

            if (categoryId != -1) {
                // Now use the categoryId in the createTransaction method
                long result = dbHelper.createTransaction(amount, currentDate, categoryId, loggedInUserId, "Income");

                if (result != -1) {
                    Toast.makeText(requireContext(), "Income added successfully!", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                } else {
                    Toast.makeText(requireContext(), "Failed to add income!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Invalid category selected!", Toast.LENGTH_SHORT).show();
            }
        } else {
            binding.incomeAmountEditText.setError("Amount is required");
        }
    }

    // Method to validate the input fields
    private boolean validateInput() {
        if (TextUtils.isEmpty(binding.autoCompleteEditText.getText().toString())) {
            binding.autoCompleteEditText.setError("Category is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.incomeAmountEditText.getText().toString())) {
            binding.incomeAmountEditText.setError("Amount is required");
            return false;
        }
        return true;
    }

    // Method to clear input fields after successful insertion
    private void clearInputFields() {
        binding.autoCompleteEditText.setText("");
        binding.incomeAmountEditText.setText("");
    }

    // Method to navigate to AddCategoryFragment
    private void navigateToAddCategoryFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerMain, new AddCategoryFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
