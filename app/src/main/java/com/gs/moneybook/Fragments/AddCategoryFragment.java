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
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentAddCategoryBinding;
import java.util.List;

public class AddCategoryFragment extends Fragment {

    private FragmentAddCategoryBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = new DBHelper(requireContext());

        // Set up the Spinner for category types
        setupCategoryTypeSpinner();

        // Load and display the list of categories
        loadCategories();

        // Handle Save Category button click
        binding.btnSaveCategory.setOnClickListener(v -> saveCategory());

        return view;
    }

    private void setupCategoryTypeSpinner() {
        // Category types: Income and Expense
        String[] categoryTypes = {"Income", "Expense"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                categoryTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoryType.setAdapter(adapter);
    }

    private void loadCategories() {
        // Get all categories (basic and user-specific) from the database
        List<String> categories = dbHelper.getAllCategories(loggedInUserId);

        // Add some hardcoded categories to the list
        categories.add("Salary");
        categories.add("Freelance");
        categories.add("Groceries");
        categories.add("Bills");
        categories.add("Savings");

        // Set up the ListView with the retrieved categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.listview_item,
                categories
        );
        binding.lvCategories.setAdapter(adapter);
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

        // Check if the category already exists in the database
        new Thread(() -> {
            try {
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
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
