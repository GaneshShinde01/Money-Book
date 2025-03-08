package com.gs.moneybook.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.R;
import com.gs.moneybook.Utilities.DateUtils;
import com.gs.moneybook.databinding.FragmentAddIncomeBinding;

import java.util.ArrayList;
import java.util.List;

public class AddIncomeFragment extends Fragment {

    private FragmentAddIncomeBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1;
    private final String ADD_NEW_CATEGORY = "Other";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddIncomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = DBHelper.getInstance(requireContext());
        loadCategories();

        binding.addIncomeButton.setOnClickListener(v -> {
            if (validateInput()) {
                addIncome();
            }
        });

        binding.autoCompleteEditText.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCategory = (String) parent.getItemAtPosition(position);
            if (selectedCategory.equals(ADD_NEW_CATEGORY)) {
                navigateToAddCategoryFragment();
            }
        });

        binding.lvCategoriesIncome.setOnItemClickListener((parent, view12, position, id) -> {
            String selectedCategory = (String) binding.lvCategoriesIncome.getItemAtPosition(position);
            binding.autoCompleteEditText.setText(selectedCategory);
        });

        return view;
    }

    private void loadCategories() {
        List<String> categoryNames = dbHelper.getCategoriesByTypeAndUserId("Income", loggedInUserId);
        categoryNames.add(ADD_NEW_CATEGORY);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
        binding.autoCompleteEditText.setAdapter(adapter);

        List<String> categoryNamesForListView = new ArrayList<>(categoryNames);
        categoryNamesForListView.remove(ADD_NEW_CATEGORY);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.listview_item, categoryNamesForListView);
        binding.lvCategoriesIncome.setAdapter(adapter2);
    }

    private void addIncome() {
        String categoryName = binding.autoCompleteEditText.getText().toString();
        String amountString = binding.incomeAmountEditText.getText().toString();
        String transDesc = binding.ettransDecriptionInc.getText().toString();

        if (TextUtils.isEmpty(amountString)) {
            Toast.makeText(requireContext(), "Please enter the amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountString);
            if (amount <= 0) {
                Toast.makeText(requireContext(), "Amount must be greater than zero.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ensureCategoryExists(categoryName)) return;

            long result = dbHelper.createTransaction(amount, DateUtils.getCurrentDateTimeForDatabase(), categoryName, loggedInUserId, "Income", "Cash",transDesc); // Payment mode is now "Cash"
            if (result != -1) {
                Toast.makeText(requireContext(), "Income added successfully!", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                Log.e("AddIncomeFragment", "Error adding income.");
                Toast.makeText(requireContext(), "Error adding income.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("AddIncomeFragment", "Invalid amount entered: " + e.getMessage());
            Toast.makeText(requireContext(), "Invalid amount entered.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean ensureCategoryExists(String categoryName) {
        if (!dbHelper.checkCategoryExists(categoryName, "Income", loggedInUserId)) {
            if (!dbHelper.insertCategory(dbHelper.getWritableDatabase(), categoryName, "Income", loggedInUserId)) {
                Log.e("AddIncomeFragment", "Failed to insert category: " + categoryName);
                Toast.makeText(requireContext(), "Failed to add category.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(binding.autoCompleteEditText.getText())) {
            Toast.makeText(requireContext(), "Please select a category.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(binding.incomeAmountEditText.getText())) {
            Toast.makeText(requireContext(), "Please enter an amount.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearInputFields() {
        binding.autoCompleteEditText.setText("");
        binding.incomeAmountEditText.setText("");
        binding.ettransDecriptionInc.setText("");
    }

    private void navigateToAddCategoryFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerMain, new AddCategoryFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}