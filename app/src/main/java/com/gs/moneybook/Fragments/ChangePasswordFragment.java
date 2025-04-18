package com.gs.moneybook.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.TestActivity;
import com.gs.moneybook.databinding.FragmentChangePasswordBinding;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;
    private DBHelper dbHelper;
    private String userEmail;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        dbHelper = DBHelper.getInstance(getContext());

        // Get the email passed from the ForgetPasswordFragment
        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
        }

        // Set up the change password button click listener
        binding.btnChangePassword.setOnClickListener(v -> {
            String newPassword = binding.etNewPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            if (validateInputs(newPassword, confirmPassword)) {
                // Update the password in the database
                boolean isUpdated = dbHelper.updatePassword(userEmail, newPassword);
                if (isUpdated) {
                    Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                    // Optionally redirect to the login screen
                    ((TestActivity)getActivity()).loadFragment(new LoginFragment(),"LoginFragment");
                } else {
                    Toast.makeText(getContext(), "Error changing password. Please try again.", Toast.LENGTH_SHORT).show();
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

    private boolean validateInputs(String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(newPassword)) {
            binding.etNewPassword.setError("New Password is required");
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.etConfirmPassword.setError("Confirm Password is required");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Passwords do not match");
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
