package com.gs.moneybook.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1
    private final String ADD_NEW_CATEGORY = "Other";
    private String categoryNameFromList;
    private String categoryName;

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


        binding.lvCategoriesIncome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                categoryNameFromList = (String) binding.lvCategoriesIncome.getItemAtPosition(position);

                binding.autoCompleteEditText.setText(categoryNameFromList);
                //Toast.makeText(requireContext(), "Category = "+categoryNameFromList, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Method to load income categories from the database
    private void loadCategories() {
        List<String> categoryNames = new ArrayList<>();

        // Get both default and user-specific categories for the logged-in user
        List<String> userCategories = dbHelper.getCategoriesByTypeAndUserId("Income", loggedInUserId);

        // Add the user categories to the list for AutoCompleteTextView
        categoryNames.addAll(userCategories);
        categoryNames.add(ADD_NEW_CATEGORY); // Add "Other" option only for AutoCompleteTextView

        // Set up the adapter for AutoCompleteTextView (suggestions dropdown)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categoryNames
        );
        binding.autoCompleteEditText.setAdapter(adapter);

        // ListView setup without the "Other" option
        List<String> categoryNamesForListView = new ArrayList<>(userCategories);  // Exclude "Other"
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.listview_item, categoryNamesForListView);
        binding.lvCategoriesIncome.setAdapter(adapter2);
    }


    // Method to add income
    private void addIncome() {
        // Get user inputs

       /* if(categoryNameFromList.isEmpty()){
             categoryName = binding.autoCompleteEditText.getText().toString();
        }else {

            binding.autoCompleteEditText.setText(categoryNameFromList);
            categoryName = binding.autoCompleteEditText.getText().toString();
        }*/
       categoryName = binding.autoCompleteEditText.getText().toString();
        String amountString = binding.incomeAmountEditText.getText().toString();

        if (!TextUtils.isEmpty(amountString)) {
            try {
                double amount = Double.parseDouble(amountString);

                if (amount > 0) {
                    // Get the current date using DateUtils
                    String currentDate = DateUtils.getCurrentDate();

                    // Ensure the category exists or add it if necessary
                    boolean categoryExists = ensureCategoryExists(categoryName);

                    if (categoryExists) {
                        // Now use the categoryName in the createTransaction method
                        long result = dbHelper.createTransaction(amount, currentDate, categoryName, loggedInUserId, "Income");

                        if (result != -1) {
                            Toast.makeText(requireContext(), "Income added successfully!", Toast.LENGTH_SHORT).show();
                            clearInputFields();
                        } else {
                            Toast.makeText(requireContext(), "Error adding income.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Amount must be greater than zero.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid amount entered.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Please enter the amount.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to ensure the category exists in the database
    private boolean ensureCategoryExists(String categoryName) {
        if (!dbHelper.checkCategoryExists(categoryName, "Income", loggedInUserId)) {
            // Insert the new category for the user
            return dbHelper.insertCategory(dbHelper.getWritableDatabase(), categoryName, "Income", loggedInUserId);
        }
        return true;
    }

    // Method to validate user input
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

    // Method to clear input fields after successfully adding income
    private void clearInputFields() {
        binding.autoCompleteEditText.setText("");
        binding.incomeAmountEditText.setText("");
    }

    // Navigate to AddCategoryFragment to allow the user to add a new category
    private void navigateToAddCategoryFragment() {
        AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerMain, addCategoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
