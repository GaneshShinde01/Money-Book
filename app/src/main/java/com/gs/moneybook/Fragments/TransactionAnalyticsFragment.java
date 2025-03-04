package com.gs.moneybook.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.Utilities.DateUtils;
import com.gs.moneybook.databinding.FragmentTransactionAnalyticsBinding;

import java.util.Calendar;

public class TransactionAnalyticsFragment extends Fragment {
    private FragmentTransactionAnalyticsBinding binding;
    private DBHelper dbHelper;
    private double income = 0.0;
    private double expense = 0.0;

    private String startDate = "";
    private String endDate = "";
    private Calendar startCalendar;  // New Calendar object to track start date

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

        //setting default dates for today
        startDate = DateUtils.getCurrentDateForDatabase();
        endDate = DateUtils.getCurrentDateForDatabase();
        calculateAndDisplayAnalytics();


        // Initialize the startCalendar as a new Calendar instance
        startCalendar = Calendar.getInstance();

        // Handle Start Date Selection
        binding.tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        // Handle End Date Selection and automatically calculate income/expense
        binding.tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });

        return view;
    }

    // Method to show the DatePickerDialog
    private void showDatePickerDialog(final boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            // Format selected date
            String selectedDate = DateUtils.formatDateForDatabase(dayOfMonth, month1, year1);

            // Store the selected date in the appropriate variable
            if (isStartDate) {
                startDate = selectedDate;
                startCalendar.set(year1, month1, dayOfMonth);  // Store the selected start date in calendar
                binding.tvStartDate.setText("Start Date: " + startDate);
            } else {
                endDate = selectedDate;
                binding.tvEndDate.setText("End Date: " + endDate);
                // After end date is selected, automatically calculate income/expense and load the graph
                calculateAndDisplayAnalytics();
            }
        }, year, month, day);

        // Set constraints for start and end dates
        if (isStartDate) {
            // If it's the start date, no constraint is needed, so we reset the min/max date
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());  // Allow selecting dates up to today
        } else {
            // If it's the end date, set the minimum selectable date to the day after the start date
            if (startCalendar != null) {
                datePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis());  // Set min date for end date
            }
        }

        datePickerDialog.show();
    }

    // Method to calculate and display income/expense and update the bar graph
    private void calculateAndDisplayAnalytics() {
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            // Calculate income and expense between selected dates
            income = dbHelper.getTotalIncome(startDate, endDate);
            expense = dbHelper.getTotalExpense(startDate, endDate);

            // Update the text views
            binding.tvIncomeTransactionAnalytics.setText(String.format("%.2f", income));
            binding.tvOutcomeTransactionAnalytics.setText(String.format("%.2f", expense));

            // Load the bar graph with the new values
            loadBarGraph();
        } else {
            Toast.makeText(requireContext(), "Please select both start and end dates!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBarGraph() {
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
