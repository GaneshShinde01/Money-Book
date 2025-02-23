package com.gs.moneybook.Fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.databinding.FragmentAddCategoryBinding;

import java.util.List;

public class AddExpenseFragment extends Fragment {

    private FragmentAddCategoryBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1
    private ArrayAdapter<String> categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = new DBHelper(requireContext());

        // Set up the Spinner for category types
        setupCategoryTypeSpinner();

        // Handle Save Category button click
        binding.btnSaveCategory.setOnClickListener(v -> saveCategory());

        // Load and display existing categories
        loadCategories();

        return view;
    }

    private void setupCategoryTypeSpinner() {
        // Category types: Income and Expense
        String[] categoryTypes = {"Income", "Expense"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoryType.setAdapter(adapter);
    }
    private void saveCategory() {
        // Get the entered category name
        String categoryName = binding.etCategoryName.getText().toString().trim();
        // Get the selected category type from the Spinner
        String categoryType = binding.spinnerCategoryType.getSelectedItem().toString();

        // Validate inputs
        if (TextUtils.isEmpty(categoryName)) {
            binding.etCategoryName.setError("Category name is required");
            return;
        }

        // Check if the category already exists
        new Thread(() -> {
            boolean categoryExists = dbHelper.checkCategoryExists(categoryName, categoryType, loggedInUserId);
            requireActivity().runOnUiThread(() -> {
                if (categoryExists) {
                    Toast.makeText(requireContext(), "Category already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert the new category into the database
                    SQLiteDatabase db = dbHelper.getWritableDatabase();  // Get writable database
                    boolean isInserted = dbHelper.insertCategory(db, categoryName, categoryType, loggedInUserId);

                    if (isInserted) {
                        Toast.makeText(requireContext(), "Category added successfully!", Toast.LENGTH_SHORT).show();
                        // Clear input fields after insertion
                        binding.etCategoryName.setText("");
                        binding.spinnerCategoryType.setSelection(0);
                        // Reload the categories list
                        loadCategories();
                    } else {
                        Toast.makeText(requireContext(), "Failed to add category. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }).start();
    }

    private void loadCategories() {
        // Fetch all categories from the database
        List<String> categories = dbHelper.getAllCategories(loggedInUserId);
        categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                categories
        );
        binding.lvCategories.setAdapter(categoryAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
