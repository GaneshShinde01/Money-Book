package com.gs.moneybook.Fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.databinding.FragmentTransactionAnalyticsBinding;

public class TransactionAnalyticsFragment extends Fragment {
    private FragmentTransactionAnalyticsBinding binding;
    private DBHelper dbHelper;
    private double income = 0.0;
    private double expense = 0.0;

    public TransactionAnalyticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentTransactionAnalyticsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dbHelper = DBHelper.getInstance(getContext());

        String startDate = "2025-03-04";
        String endDate = "2025-03-04";

         income = dbHelper.getTotalIncome(startDate, endDate);
         expense = dbHelper.getTotalExpense(startDate, endDate);


        Toast.makeText(getContext(), "income="+income, Toast.LENGTH_SHORT).show();

         binding.tvIncomeTransactionAnalytics.setText(income+"");
         binding.tvOutcomeTransactionAnalytics.setText(expense+"");


        loadBarGraph();
        return view;
    }

    private void loadBarGraph() {
        // Example values for income and expense
//        int income = 500000;  // Example value for income
//        int expense = 300000;  // Example value for expense

        // Maximum value between income and expense
        int maxValue = (int) Math.max(income, expense);

        // If the maxValue is 0 (to avoid division by zero)
        if (maxValue == 0) {
            maxValue = 1;  // Set to 1 to avoid division by zero
        }

        // Desired height of the bar container in dp (300dp in this example)
        int barContainerHeight = 300;  // in dp

        // Scale factor (how much 1 unit of value equals in terms of dp)
        float scaleFactor = (float) maxValue / barContainerHeight;

        // Calculate the scaled height for income and expense
        int scaledIncomeHeight = (int) (income / scaleFactor);
        int scaledExpenseHeight = (int) (expense / scaleFactor);

        // Convert dp to pixels (since the height property uses pixels)
        int incomeHeightInPixels = dpToPx(scaledIncomeHeight);
        int expenseHeightInPixels = dpToPx(scaledExpenseHeight);

        // Set the height for the income bar dynamically using ViewBinding
        LinearLayout.LayoutParams incomeParams = (LinearLayout.LayoutParams) binding.barIncome.getLayoutParams();
        incomeParams.height = incomeHeightInPixels;  // Set scaled height for income
        incomeParams.width = dpToPx(60);  // Set fixed width for the bar
        incomeParams.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5)); // Add margins for better spacing
        binding.barIncome.setLayoutParams(incomeParams);

        // Set the height for the expense bar dynamically using ViewBinding
        LinearLayout.LayoutParams expenseParams = (LinearLayout.LayoutParams) binding.barExpense.getLayoutParams();
        expenseParams.height = expenseHeightInPixels;  // Set scaled height for expense
        expenseParams.width = dpToPx(60);  // Set fixed width for the bar
        expenseParams.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5)); // Add margins for better spacing
        binding.barExpense.setLayoutParams(expenseParams);

        // Set the income and expense text values using ViewBinding
        binding.incomeValueText.setText(String.valueOf(income));
        binding.expenseValueText.setText(String.valueOf(expense));

        // Optionally, adjust text size for better visibility
        binding.incomeValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);  // Adjust text size
        binding.expenseValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);  // Adjust text size
        binding.incomeLabelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);  // Adjust label size
        binding.expenseLabelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);  // Adjust label size
    }

    // Convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
