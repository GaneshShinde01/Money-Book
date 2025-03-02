package com.gs.moneybook.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper =  DBHelper.getInstance(requireContext());


        populateFields();

        binding.btnSaveChanges.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void populateFields() {

        Cursor cursor = dbHelper.getUserById(loggedInUserId);

        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
            String mobileNumber = cursor.getString(cursor.getColumnIndexOrThrow("mobileNumber"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String occupation = cursor.getString(cursor.getColumnIndexOrThrow("occupation"));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow("dob"));
            int  monthlyIncome = (int) cursor.getDouble(cursor.getColumnIndexOrThrow("monthlyIncome"));
            int savingsGoal = (int) cursor.getDouble(cursor.getColumnIndexOrThrow("savingsGoal"));
            String currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"));

            binding.etFullNameEdit.setText(fullName);
            binding.etMobileNumberEdit.setText(mobileNumber);
            binding.etEmailEdit.setText(email);
            binding.etOccupationEdit.setText(occupation);
            binding.etDOBEdit.setText(dob);
            binding.etIncomeEdit.setText(String.valueOf(monthlyIncome));
            binding.etSavingsGoalEdit.setText(String.valueOf(savingsGoal));
            binding.etCurrencyEdit.setText(currency);

            cursor.close();
        }
    }

    private void saveChanges() {
        String fullName = binding.etFullNameEdit.getText().toString().trim();
        String mobileNumber = binding.etMobileNumberEdit.getText().toString().trim();
        String occupation = binding.etOccupationEdit.getText().toString().trim();
        String income = binding.etIncomeEdit.getText().toString().trim();
        String savings = binding.etSavingsGoalEdit.getText().toString().trim();
        String currency = binding.etCurrencyEdit.getText().toString().trim();

        if (fullName.isEmpty() || mobileNumber.isEmpty() || occupation.isEmpty() || income.isEmpty() || savings.isEmpty() || currency.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("fullName", fullName);
        values.put("mobileNumber", mobileNumber);
        values.put("occupation", occupation);
        values.put("monthlyIncome", Double.parseDouble(income));
        values.put("savingsGoal", Double.parseDouble(savings));
        values.put("currency", currency);

        boolean rowsAffected = dbHelper.updateUser(values,loggedInUserId);

        if (rowsAffected) {
            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack(); // Go back to profile view
            }

        } else {
            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}