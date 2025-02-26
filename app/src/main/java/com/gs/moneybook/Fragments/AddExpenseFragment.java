package com.gs.moneybook.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.R;
import com.gs.moneybook.Utilities.DateUtils;
import com.gs.moneybook.databinding.FragmentAddExpenseBinding;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseFragment extends Fragment {

    private FragmentAddExpenseBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1
    private final String ADD_NEW_CATEGORY = "Other";
    private final String ADD_NEW_PAYMENT_MODE = "Other"; // Option for user to add new payment mode
    private String categoryNameFromList;
    private String categoryName;
    private String paymentModeFromList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = new DBHelper(requireContext());

        // Load categories for the logged-in user from the database
        loadCategories();

        // Load payment modes
        loadPaymentModes();

        // Add Expense button click listener
        binding.addExpenseButton.setOnClickListener(v -> {
            if (validateInput()) {
                // Add expense to the database
                addExpense();
            }
        });

        // Listen for category selection in AutoCompleteTextView
        binding.autoCompleteEditTextForExpenseCategory.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCategory = (String) parent.getItemAtPosition(position);
            if (selectedCategory.equals(ADD_NEW_CATEGORY)) {
                // Redirect to AddCategoryFragment
                navigateToAddCategoryFragment();
            }
        });

        // Listen for payment mode selection
        binding.autoCompleteEditTextExpensePaymentMode.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedPaymentMode = (String) parent.getItemAtPosition(position);
            if (selectedPaymentMode.equals(ADD_NEW_PAYMENT_MODE)) {
                // Redirect to AddPaymentModeFragment
                navigateToAddPaymentModeFragment();
            }
        });

        binding.lvCategoriesExpense.setOnItemClickListener((parent, view12, position, id) -> {
            categoryNameFromList = (String) binding.lvCategoriesExpense.getItemAtPosition(position);
            binding.autoCompleteEditTextForExpenseCategory.setText(categoryNameFromList);
        });

        return view;
    }

    // Method to load expense categories from the database
    private void loadCategories() {
        List<String> categoryNames = new ArrayList<>();

        // Get both default and user-specific categories for the logged-in user
        List<String> userCategories = dbHelper.getCategoriesByTypeAndUserId("Expense", loggedInUserId);

        // Add the user categories to the list for AutoCompleteTextView
        categoryNames.addAll(userCategories);
        categoryNames.add(ADD_NEW_CATEGORY); // Add "Other" option only for AutoCompleteTextView

        // Set up the adapter for AutoCompleteTextView (suggestions dropdown)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categoryNames
        );
        binding.autoCompleteEditTextForExpenseCategory.setAdapter(adapter);

        // ListView setup without the "Other" option
        List<String> categoryNamesForListView = new ArrayList<>(userCategories);  // Exclude "Other"
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.listview_item, categoryNamesForListView);
        binding.lvCategoriesExpense.setAdapter(adapter2);
    }

    // Method to load payment modes from the database
    private void loadPaymentModes() {
        List<String> paymentModes = new ArrayList<>();

        // Fetch payment modes from the database
        List<String> storedPaymentModes = dbHelper.getPaymentModes(loggedInUserId);

        // Add the default payment modes to the list
        paymentModes.addAll(storedPaymentModes);
        paymentModes.add(ADD_NEW_PAYMENT_MODE); // Add "Other" option for AutoCompleteTextView

        // Set up the adapter for payment mode AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                paymentModes
        );
        binding.autoCompleteEditTextExpensePaymentMode.setAdapter(adapter);
    }

    // Method to add an expense
    private void addExpense() {
        // Get user inputs
        categoryName = binding.autoCompleteEditTextForExpenseCategory.getText().toString();
        String amountString = binding.expenseAmountEditText.getText().toString();
        String paymentMode = binding.autoCompleteEditTextExpensePaymentMode.getText().toString();

        if (!TextUtils.isEmpty(amountString)) {
            try {
                double amount = Double.parseDouble(amountString);

                if (amount > 0) {
                    // Get the current date using DateUtils
                    String currentDate = DateUtils.getCurrentDate();

                    // Ensure the category exists or add it if necessary
                    boolean categoryExists = ensureCategoryExists(categoryName);

                    // Ensure payment mode exists or add it if necessary
                    boolean paymentModeExists = ensurePaymentModeExists(paymentMode);

                    if (categoryExists && paymentModeExists) {
                        // Use categoryName and paymentMode in the createTransaction method
                        long result = dbHelper.createTransaction(amount, currentDate, categoryName, loggedInUserId, "Expense", paymentMode);

                        if (result != -1) {
                            Toast.makeText(requireContext(), "Expense added successfully!", Toast.LENGTH_SHORT).show();
                            clearInputFields();
                        } else {
                            Toast.makeText(requireContext(), "Error adding expense.", Toast.LENGTH_SHORT).show();
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
        if (!dbHelper.checkCategoryExists(categoryName, "Expense", loggedInUserId)) {
            // Insert the new category for the user
            return dbHelper.insertCategory(dbHelper.getWritableDatabase(), categoryName, "Expense", loggedInUserId);
        }
        return true;
    }

    // Method to ensure the payment mode exists in the database
    private boolean ensurePaymentModeExists(String paymentMode) {
        if (!dbHelper.checkPaymentModeExists(paymentMode, loggedInUserId)) {
            // Insert the new payment mode
            return dbHelper.insertPaymentMode(paymentMode, loggedInUserId);
        }
        return true;
    }

    // Method to validate user input
    private boolean validateInput() {
        if (TextUtils.isEmpty(binding.autoCompleteEditTextForExpenseCategory.getText())) {
            Toast.makeText(requireContext(), "Please select a category.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(binding.expenseAmountEditText.getText())) {
            Toast.makeText(requireContext(), "Please enter an amount.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(binding.autoCompleteEditTextExpensePaymentMode.getText())) {
            Toast.makeText(requireContext(), "Please select a payment mode.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Method to clear input fields after successfully adding an expense
    private void clearInputFields() {
        binding.autoCompleteEditTextForExpenseCategory.setText("");
        binding.expenseAmountEditText.setText("");
        binding.autoCompleteEditTextExpensePaymentMode.setText("");
    }

    // Navigate to AddCategoryFragment to allow the user to add a new category
    private void navigateToAddCategoryFragment() {
        AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerMain, addCategoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Navigate to AddPaymentModeFragment to allow the user to add a new payment mode
    private void navigateToAddPaymentModeFragment() {
        AddPaymentModeFragment addPaymentModeFragment = new AddPaymentModeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerMain, addPaymentModeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
