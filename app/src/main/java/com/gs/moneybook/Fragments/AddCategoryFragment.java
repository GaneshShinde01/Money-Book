package com.gs.moneybook.Fragments;

import android.app.AlertDialog;
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
    private ArrayAdapter<String> adapter;
    private List<String> userDefinedCategories;  // Store user-defined categories separately
    private List<String> allCategories;  // Store all categories (system + user-defined)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = DBHelper.getInstance(    requireContext());

        // Set up the Spinner for category types
        setupCategoryTypeSpinner();

        // Load and display the list of categories
        loadCategories();

        // Handle Save Category button click
        binding.btnSaveCategory.setOnClickListener(v -> saveCategory());

        // Set up long click listener for ListView items
        binding.lvCategories.setOnItemLongClickListener((parent, view1, position, id) -> {
            String selectedCategory = allCategories.get(position);

            // Extract the category name and type (split based on the "(Income)" or "(Expense)" suffix)
            String categoryName = selectedCategory.split(" \\(")[0];
            String categoryType = selectedCategory.contains("(Income)") ? "Income" : "Expense";

            // Check if the selected category is user-defined
            if (userDefinedCategories.contains(categoryName)) {
                showDeleteConfirmationDialog(categoryName, categoryType); // Pass category name and type
            } else {
                Toast.makeText(requireContext(), "System-defined categories cannot be deleted", Toast.LENGTH_SHORT).show();
            }

            return true;
        });

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
        // Load all categories (system-defined + user-defined)
        allCategories = dbHelper.getAllCategories(loggedInUserId);
        // Load only user-defined categories
        userDefinedCategories = dbHelper.getUserDefinedCategories(loggedInUserId);

        // Set up the ListView with all categories
        adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.listview_item,
                allCategories
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

    private void showDeleteConfirmationDialog(String categoryName , String categoryType) {
        // Create an AlertDialog to confirm deletion
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete the " + categoryType + " category: " + categoryName + "?")
                .setPositiveButton("Yes", (dialog, which) -> deleteCategory(categoryName, categoryType))  // Pass both name and type
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteCategory(String categoryName , String categoryType) {
        // Delete the category from the database
        new Thread(() -> {
            try {
                boolean isDeleted = dbHelper.deleteCategory(categoryName, categoryType, loggedInUserId); // Pass category type for deletion
                requireActivity().runOnUiThread(() -> {
                    if (isDeleted) {
                        Toast.makeText(requireContext(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                        // Reload the categories list after deletion
                        loadCategories();
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete category. Try again.", Toast.LENGTH_SHORT).show();
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
