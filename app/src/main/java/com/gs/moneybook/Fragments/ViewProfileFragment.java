package com.gs.moneybook.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.R;
import com.gs.moneybook.databinding.FragmentViewProfileBinding;

public class ViewProfileFragment extends Fragment {

    private FragmentViewProfileBinding binding;
    private int loggedInUserId = 1; // Assuming logged-in user ID is 1
    private DBHelper dbHelper;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        dbHelper =  DBHelper.getInstance(requireContext());
        displayUserProfile();

        binding.btnEditProfile.setOnClickListener(v -> {
            // Navigate to EditProfileFragment
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerMain, new EditProfileFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void displayUserProfile() {

        Cursor cursor = dbHelper.getUserById(loggedInUserId);

        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
            String mobileNumber = cursor.getString(cursor.getColumnIndexOrThrow("mobileNumber"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String occupation = cursor.getString(cursor.getColumnIndexOrThrow("occupation"));
            String dob = cursor.getString(cursor.getColumnIndexOrThrow("dob"));
            double monthlyIncome = cursor.getDouble(cursor.getColumnIndexOrThrow("monthlyIncome"));
            double savingsGoal = cursor.getDouble(cursor.getColumnIndexOrThrow("savingsGoal"));
            String currency = cursor.getString(cursor.getColumnIndexOrThrow("currency"));

            binding.tvFullNameProfile.setText(fullName);
            binding.tvMobileNumberProfile.setText(mobileNumber);
            binding.tvEmailProfile.setText(email);
            binding.tvOccupationProfile.setText(occupation);
            binding.tvDOBProfile.setText(dob);
            binding.tvIncomeProfile.setText(String.valueOf(monthlyIncome));
            binding.tvSavingsGoalProfile.setText(String.valueOf(savingsGoal));
            binding.tvCurrencyProfile.setText(currency);

            cursor.close();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

    //view profile check
}