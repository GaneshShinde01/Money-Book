package com.gs.moneybook.Fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gs.moneybook.Database.DBHelper;
import com.gs.moneybook.Model.TransactionModel;
import com.gs.moneybook.Utilities.DateUtils;
import com.gs.moneybook.databinding.FragmentTransactionAnalyticsBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class TransactionAnalyticsFragment extends Fragment {
    private FragmentTransactionAnalyticsBinding binding;
    private DBHelper dbHelper;
    private double income = 0.0;
    private double expense = 0.0;
    private int loggedInUserId = 1;

    private String startDate = "";
    private String endDate = "";
    private Calendar startCalendar;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    downloadTransactionReport();
                } else {
                    Toast.makeText(getContext(), "Storage permission denied. Cannot save PDF.", Toast.LENGTH_SHORT).show();
                }
            });

    public TransactionAnalyticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionAnalyticsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dbHelper = DBHelper.getInstance(getContext());

        startDate = DateUtils.getCurrentDateForDatabase();
        endDate = DateUtils.getCurrentDateForDatabase();
        calculateAndDisplayAnalytics();

        binding.btnDownload.setOnClickListener(v -> checkStoragePermissionAndDownload());

        startCalendar = Calendar.getInstance();

        binding.tvStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        binding.tvEndDate.setOnClickListener(v -> showDatePickerDialog(false));

        return view;
    }

    private void showDatePickerDialog(final boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            String selectedDate = DateUtils.formatDateForDatabase(dayOfMonth, month1, year1);

            if (isStartDate) {
                startDate = selectedDate;
                startCalendar.set(year1, month1, dayOfMonth);
                binding.tvStartDate.setText("Start Date: " + startDate);
            } else {
                endDate = selectedDate;
                binding.tvEndDate.setText("End Date: " + endDate);
                calculateAndDisplayAnalytics();
            }
        }, year, month, day);

        if (isStartDate) {
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        } else {
            if (startCalendar != null) {
                datePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
            }
        }

        datePickerDialog.show();
    }

    private void calculateAndDisplayAnalytics() {
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            income = dbHelper.getTotalIncome(loggedInUserId,startDate, endDate);
            expense = dbHelper.getTotalExpense(loggedInUserId,startDate, endDate);

            binding.tvIncomeTransactionAnalytics.setText(String.format("%.2f", income));
            binding.tvOutcomeTransactionAnalytics.setText(String.format("%.2f", expense));

            loadBarGraph();
        } else {
            Toast.makeText(requireContext(), "Please select both start and end dates!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBarGraph() {
        int maxValue = (int) Math.max(income, expense);
        if (maxValue == 0) {
            maxValue = 1;
        }

        int barContainerHeight = 300;
        float scaleFactor = (float) maxValue / barContainerHeight;

        int scaledIncomeHeight = (int) (income / scaleFactor);
        int scaledExpenseHeight = (int) (expense / scaleFactor);

        int incomeHeightInPixels = dpToPx(scaledIncomeHeight);
        int expenseHeightInPixels = dpToPx(scaledExpenseHeight);

        LinearLayout.LayoutParams incomeParams = (LinearLayout.LayoutParams) binding.barIncome.getLayoutParams();
        incomeParams.height = incomeHeightInPixels;
        incomeParams.width = dpToPx(60);
        incomeParams.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5));
        binding.barIncome.setLayoutParams(incomeParams);

        LinearLayout.LayoutParams expenseParams = (LinearLayout.LayoutParams) binding.barExpense.getLayoutParams();
        expenseParams.height = expenseHeightInPixels;
        expenseParams.width = dpToPx(60);
        expenseParams.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5));
        binding.barExpense.setLayoutParams(expenseParams);

        binding.incomeValueText.setText(String.valueOf(income));
        binding.expenseValueText.setText(String.valueOf(expense));

        binding.incomeValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        binding.expenseValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        binding.incomeLabelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        binding.expenseLabelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void checkStoragePermissionAndDownload() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // No need to request WRITE_EXTERNAL_STORAGE permission for Android 10 (API 29) and above.
            downloadTransactionReport();
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                downloadTransactionReport();
            } else {
                // Request storage permission for Android 9 (API 28) and below
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void downloadTransactionReport() {
        // Get the transactions for the logged-in user within the selected date range
        List<TransactionModel> transactions = dbHelper.getAllTransactionsForPDF(loggedInUserId, startDate, endDate);

        // Check if the list is empty
        if (transactions == null || transactions.isEmpty()) {
            // Show a message and return without creating the PDF
            Toast.makeText(getContext(), "No transactions available for the selected date range.", Toast.LENGTH_SHORT).show();
            return;
        }


        // Retrieve total income, expense, and turnover
        double totalIncome = dbHelper.getTotalIncome(loggedInUserId,startDate, endDate);
        double totalExpense = dbHelper.getTotalExpense(loggedInUserId,startDate, endDate);
        double totalTurnover = dbHelper.getTotalTransactionAmount(loggedInUserId, startDate, endDate);


        // Using Downloads folder for easier access
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File moneyBookFolder = new File(downloadsFolder, "MoneyBook");

        // Create the MoneyBook folder if it doesn't exist
        if (!moneyBookFolder.exists()) {
            boolean folderCreated = moneyBookFolder.mkdirs();
            if (!folderCreated) {
                Toast.makeText(getContext(), "Failed to create MoneyBook folder.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Generate a unique file name using the current date and time
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String fileName = "TransactionReport_" + timeStamp + ".pdf";

        // File path for the PDF with current date and time appended
        File pdfFile = new File(moneyBookFolder, fileName);

        // Proceed to generate and save the PDF
        generatePdfAndSave(pdfFile, transactions, totalIncome, totalExpense, totalTurnover);
    }


    private void generatePdfAndSave(File pdfFile, List<TransactionModel> transactions, double totalIncome, double totalExpense, double totalTurnover) {
        PdfDocument pdfDocument = new PdfDocument();

        int pageWidth = 1000;
        int pageHeight = 600;
        int rowsPerPage = 25;
        int yPosition = 120;
        int currentPage = 1;

        Paint paint = new Paint();

        try {
            // Create the first page
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPage).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            paint.setColor(Color.BLACK);
            paint.setTextSize(14);

            // Draw totals at the top of the page
            drawTotalsAtTop(canvas, paint, totalIncome, totalExpense, totalTurnover);

            // Draw the table headers
            drawTableHeaders(canvas, paint);

            // Draw transactions
            for (int i = 0; i < transactions.size(); i += rowsPerPage) {
                if (i > 0) { // For subsequent pages, start new page
                    pdfDocument.finishPage(page);
                    currentPage++;
                    pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPage).create();
                    page = pdfDocument.startPage(pageInfo);
                    canvas = page.getCanvas();
                }

                int startIndex = i;
                int endIndex = Math.min(i + rowsPerPage, transactions.size());
                drawTransactionsOnPage(canvas, paint, transactions.subList(startIndex, endIndex), yPosition);
            }

            pdfDocument.finishPage(page);

            // Save PDF to file
            try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
                pdfDocument.writeTo(outputStream);
                Toast.makeText(getContext(), "PDF saved successfully in MoneyBook folder!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("PDFError", "Error saving PDF: " + e.getMessage());
            Toast.makeText(getContext(), "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }

    private void drawTotalsAtTop(Canvas canvas, Paint paint, double totalIncome, double totalExpense, double totalTurnover) {
        int yPosition = 30; // Adjust Y position as needed for spacing
        paint.setTextSize(18);

        // Draw the totals
        canvas.drawText("Total Income: " + totalIncome, 10, yPosition, paint);
        yPosition += 20;
        canvas.drawText("Total Expense: " + totalExpense, 10, yPosition, paint);
        yPosition += 20;
        canvas.drawText("Total Turnover: " + totalTurnover, 10, yPosition, paint);

        // Add some space after totals
        yPosition += 30;
    }


    private void drawTableHeaders(Canvas canvas, Paint paint) {
        canvas.drawText("ID", 10, 100, paint);
        canvas.drawText("Amount", 50, 100, paint);
        canvas.drawText("Date", 160, 100, paint);
        canvas.drawText("Category", 350, 100, paint);
        canvas.drawText("Type", 520, 100, paint);
        canvas.drawText("Description", 630, 100, paint);
        canvas.drawText("PaymentMode",740,100,paint);
    }

    private void drawTransactionsOnPage(Canvas canvas, Paint paint, List<TransactionModel> transactions, int initialYPosition) {
        int yPosition = initialYPosition;


        for (TransactionModel transaction : transactions) {
            canvas.drawText(String.valueOf(transaction.getTransactionId()), 10, yPosition, paint);
            canvas.drawText(String.valueOf(transaction.getTransactionAmount()), 50, yPosition, paint);
            canvas.drawText(transaction.getTransactionDate(), 160, yPosition, paint);
            canvas.drawText(transaction.getCategoryName(), 350, yPosition, paint);
            canvas.drawText(transaction.getTransactionType(), 520, yPosition, paint);
            canvas.drawText(transaction.getTransactionDescription(), 630, yPosition, paint);
            canvas.drawText(transaction.getPaymentModeName(), 740, yPosition, paint);
            yPosition += 20;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
