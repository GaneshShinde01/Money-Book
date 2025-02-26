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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentAddPaymentModeBinding;

import java.util.List;

public class AddPaymentModeFragment extends Fragment {

    private FragmentAddPaymentModeBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1
    private ArrayAdapter<String> adapter;
    private List<String> userDefinedPaymentModes;  // Store user-defined PaymentModes separately
    private List<String> allPaymentModes;  // Store all PaymentModes (system + user-defined)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddPaymentModeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper = new DBHelper(requireContext());

        // Load and display the list of payment modes
        loadPaymentModes();

        // Handle Save Payment Mode button click
        binding.btnSavePaymentMode.setOnClickListener(v -> savePaymentMode());

        // Set up long click listener for ListView items
        binding.lvPaymentMode.setOnItemLongClickListener((parent, view1, position, id) -> {
            String selectedPaymentMode = allPaymentModes.get(position);

            // Check if the selected payment mode is user-defined
            if (userDefinedPaymentModes.contains(selectedPaymentMode)) {
                showDeleteConfirmationDialog(selectedPaymentMode);
            } else {
                Toast.makeText(requireContext(), "System-defined payment modes cannot be deleted", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        return view;
    }

    private void loadPaymentModes() {
        // Load all payment modes (system-defined + user-defined)
        allPaymentModes = dbHelper.getPaymentModes(loggedInUserId);
        // Load only user-defined payment modes
        userDefinedPaymentModes = dbHelper.getUserDefinedPaymentModes(loggedInUserId);

        // Set up the ListView with all payment modes
        adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.listview_item,
                allPaymentModes
        );
        binding.lvPaymentMode.setAdapter(adapter);
    }

    private void savePaymentMode() {
        // Get the entered payment mode name
        String paymentModeName = binding.etPaymentModeName.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(paymentModeName)) {
            binding.etPaymentModeName.setError("Payment mode name is required");
            return;
        }

        // Check if the payment mode already exists in the database for this user or system-wide
        new Thread(() -> {
            try {
                boolean paymentModeExists = dbHelper.checkPaymentModeExists(paymentModeName, loggedInUserId);
                requireActivity().runOnUiThread(() -> {
                    if (paymentModeExists) {
                        Toast.makeText(requireContext(), "Payment mode already exists!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Insert the new payment mode for the current user
                        SQLiteDatabase db = dbHelper.getWritableDatabase();  // Get writable database
                        boolean isInserted = dbHelper.insertPaymentMode(paymentModeName, loggedInUserId);

                        if (isInserted) {
                            Toast.makeText(requireContext(), "Payment mode added successfully!", Toast.LENGTH_SHORT).show();
                            // Clear input fields after insertion
                            binding.etPaymentModeName.setText("");
                            // Reload the payment modes list
                            loadPaymentModes();
                        } else {
                            Toast.makeText(requireContext(), "Failed to add payment mode. Try again.", Toast.LENGTH_SHORT).show();
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

    private void showDeleteConfirmationDialog(String paymentModeName) {
        // Create an AlertDialog to confirm deletion
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Payment Mode")
                .setMessage("Are you sure you want to delete the payment mode: " + paymentModeName + "?")
                .setPositiveButton("Yes", (dialog, which) -> deletePaymentMode(paymentModeName))
                .setNegativeButton("No", null)
                .show();
    }

    private void deletePaymentMode(String paymentModeName) {
        // Delete the payment mode from the database
        new Thread(() -> {
            try {
                boolean isDeleted = dbHelper.deletePaymentMode(paymentModeName, loggedInUserId);
                requireActivity().runOnUiThread(() -> {
                    if (isDeleted) {
                        Toast.makeText(requireContext(), "Payment mode deleted successfully!", Toast.LENGTH_SHORT).show();
                        // Reload the payment modes list after deletion
                        loadPaymentModes();
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete payment mode. Try again.", Toast.LENGTH_SHORT).show();
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
