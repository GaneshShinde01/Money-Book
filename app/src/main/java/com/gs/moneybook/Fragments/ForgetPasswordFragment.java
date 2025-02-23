package com.gs.moneybook.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentForgetPasswordBinding;
import java.util.Calendar;

public class ForgetPasswordFragment extends Fragment {

    private FragmentForgetPasswordBinding binding;
    private DBHelper dbHelper;
    private Calendar calendar;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false);
        dbHelper = new DBHelper(getContext());
        calendar = Calendar.getInstance();

        // Set up the DatePicker for DOB
        binding.etDOB.setOnClickListener(v -> showDatePickerDialog());

        // Set up the submit button click listener
        binding.btnSubmit.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String dob = binding.etDOB.getText().toString().trim();

            if (validateInputs(email, dob)) {
                if (dbHelper.checkUserExists(email)) {
                    boolean isDobCorrect = dbHelper.verifyUserDob(email, dob);
                    if (isDobCorrect) {
                        // If DOB matches, allow the user to proceed to the ChangePasswordFragment
                        Toast.makeText(getContext(), "DOB verified. Please change your password.", Toast.LENGTH_SHORT).show();
                        // Navigate to ChangePasswordFragment and pass the email
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                        changePasswordFragment.setArguments(bundle);
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.containerTest, changePasswordFragment,"ChangePasswordFragment")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Incorrect DOB. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Handling back press
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Check if there are any fragments in the back stack
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    // Go back to the previous fragment
                    getActivity().getSupportFragmentManager().popBackStack(); // Use popBackStack instead of popBackStackImmediate
                } else {
                    // If no fragments in the back stack, use the default back press behavior (exit activity)
                    requireActivity().onBackPressed();
                }
            }
        });

        return binding.getRoot();
    }

    // Method to show the DatePickerDialog
    private void showDatePickerDialog() {
        // Get the current date to initialize the DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and set it in the EditText (dd/MM/yyyy)
                    String formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    binding.etDOB.setText(formattedDate); // Set formatted date in EditText
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private boolean validateInputs(String email, String dob) {
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(dob)) {
            binding.etDOB.setError("Date of Birth is required");
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
