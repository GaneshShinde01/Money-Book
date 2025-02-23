package com.gs.moneybook.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.Model.UserModel;
import com.gs.moneybook.R;
import com.gs.moneybook.TestActivity;
import com.gs.moneybook.databinding.FragmentRegisterBinding;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private DBHelper DBHelper;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize ViewBinding
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize the database helper
        DBHelper = new DBHelper(requireContext());

        // Hardcoded list of currencies
        List<String> currencyList = Arrays.asList(
                "INR - Indian Rupee",
                "USD - United States Dollar",
                "EUR - Euro",
                "GBP - British Pound",
                "JPY - Japanese Yen",
                "AUD - Australian Dollar",
                "CAD - Canadian Dollar",
                "CNY - Chinese Yuan",
                "BRL - Brazilian Real",
                "ZAR - South African Rand"
        );

        // Populate Spinner with the hardcoded list using ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                currencyList
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding.spinnerCurrency.setAdapter(adapter);

        binding.etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Register button click listener
        binding.btnRegister.setOnClickListener(v -> {
            // Perform validation
            if (validateInput()) {
                // Get user input data
                String fullName = binding.etFullName.getText().toString();
                String mobileNumber = binding.etMobileNumber.getText().toString();
                String email = binding.etEmail.getText().toString();
                String occupation = binding.etOccupation.getText().toString();
                String password = binding.etPassword.getText().toString();
                String dob = binding.etDOB.getText().toString();
                double monthlyIncome = Double.parseDouble(binding.etIncome.getText().toString());
                double savingsGoal = Double.parseDouble(binding.etSavingsGoal.getText().toString());
                String currency = binding.spinnerCurrency.getSelectedItem().toString();

                // Check if the user already exists using the provided email
                if (DBHelper.checkUserExists(email)) {
                    Toast.makeText(requireContext(), "User with this email already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Create UserModel object
                    UserModel user = new UserModel(
                            fullName,
                            mobileNumber,
                            email,
                            occupation,
                            password,
                            dob,
                            monthlyIncome,
                            savingsGoal,
                            currency
                    );

                    // Insert data into the SQLite database
                    long isInserted = DBHelper.insertUser(user);

                    if (isInserted != -1) {
                        Toast.makeText(requireContext(), "User registered successfully!", Toast.LENGTH_SHORT).show();
                        // Clear input fields after successful registration
                        clearInputFields();
                    } else {
                        Toast.makeText(requireContext(), "Registration failed, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestActivity)getActivity()).loadFragment(new LoginFragment());
            }
        });

        return view;
    }

    // Validation logic
    private boolean validateInput() {
        if (TextUtils.isEmpty(binding.etFullName.getText().toString())) {
            binding.etFullName.setError("Full Name is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.etMobileNumber.getText().toString())) {
            binding.etMobileNumber.setError("Mobile Number is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
            binding.etEmail.setError("Email is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.etOccupation.getText().toString())) {
            binding.etOccupation.setError("Occupation is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.etPassword.getText().toString())) {
            binding.etPassword.setError("Password is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.etDOB.getText().toString())) {
            binding.etDOB.setError("Date of Birth is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.etIncome.getText().toString())) {
            binding.etIncome.setError("Monthly Income is required");
            return false;
        }
        if (TextUtils.isEmpty(binding.etSavingsGoal.getText().toString())) {
            binding.etSavingsGoal.setError("Savings Goal is required");
            return false;
        }
        return true;
    }

    // Clear input fields after registration
    private void clearInputFields() {
        binding.etFullName.setText("");
        binding.etMobileNumber.setText("");
        binding.etEmail.setText("");
        binding.etOccupation.setText("");
        binding.etPassword.setText("");
        binding.etDOB.setText("");
        binding.etIncome.setText("");
        binding.etSavingsGoal.setText("");
        binding.spinnerCurrency.setSelection(0);
    }

    private void showDatePickerDialog() {
        // Get the current date to initialize the DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Format the date and set it in the EditText
                        String formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        binding.etDOB.setText(formattedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();


    }

}
