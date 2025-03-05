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
import com.gs.moneybook.Model.InvestmentModel;
import com.gs.moneybook.Utilities.DateUtils;
import com.gs.moneybook.databinding.FragmentInvestmentHistoryBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class InvestmentHistoryFragment extends Fragment {

    private FragmentInvestmentHistoryBinding binding;
    private DBHelper dbHelper;
    private int loggedInUserId = 1; // Assuming you have a logged-in user ID

    private double totalExpense = 0.0;
    private double totalInvestment = 0.0;

    private String startDate = "";
    private String endDate = "";
    private Calendar startCalendar;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    downloadInvestmentReport();
                } else {
                    Toast.makeText(getContext(), "Storage permission denied. Cannot save PDF.", Toast.LENGTH_SHORT).show();
                }
            });

    public InvestmentHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInvestmentHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dbHelper = DBHelper.getInstance(getContext());

        startDate = DateUtils.getCurrentDateForDatabase();
        endDate = DateUtils.getCurrentDateForDatabase();

        // Fetch total expense and investment
        fetchExpenseAndInvestmentData();

        binding.btnDownloadInvestmentReport.setOnClickListener(v -> checkStoragePermissionAndDownload());

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
                fetchExpenseAndInvestmentData();
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


    private void fetchExpenseAndInvestmentData() {

        if(!startDate.isEmpty() && !endDate.isEmpty()) {


            // Assuming DBHelper has methods to get total investment and expense
            totalExpense = dbHelper.getTotalExpense(loggedInUserId, startDate, endDate); // Fetch total expense
            System.out.println(totalExpense);
            totalInvestment = dbHelper.getTotalInvestment(loggedInUserId, startDate, endDate); // Fetch total investment
            System.out.println(totalInvestment);

            // Display the values in the TextViews
            binding.tvTotalInvestment.setText(String.format("%.2f", totalInvestment));

            loadBarGraph(); //loading the bar graph


        }else {
            Toast.makeText(requireContext(), "Please select both start and end dates!", Toast.LENGTH_SHORT).show();

        }
    }

    private void loadBarGraph() {
        int maxValue = (int) Math.max(totalExpense, totalInvestment);
        if (maxValue == 0) {
            maxValue = 1;
        }

        int barContainerHeight = 300;
        float scaleFactor = (float) maxValue / barContainerHeight;

        int scaledExpenseHeight = (int) (totalExpense / scaleFactor);
        int scaledInvestmentHeight = (int) (totalInvestment / scaleFactor);

        int expenseHeightInPixels = dpToPx(scaledExpenseHeight);
        int investmentHeightInPixels = dpToPx(scaledInvestmentHeight);

        LinearLayout.LayoutParams expenseParams = (LinearLayout.LayoutParams) binding.barExpense.getLayoutParams();
        expenseParams.height = expenseHeightInPixels;
        expenseParams.width = dpToPx(60);
        expenseParams.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5));
        binding.barExpense.setLayoutParams(expenseParams);

        LinearLayout.LayoutParams investmentParams = (LinearLayout.LayoutParams) binding.barInvestment.getLayoutParams();
        investmentParams.height = investmentHeightInPixels;
        investmentParams.width = dpToPx(60);
        investmentParams.setMargins(dpToPx(10), dpToPx(5), dpToPx(10), dpToPx(5));
        binding.barInvestment.setLayoutParams(investmentParams);

        binding.expenseValueText.setText(String.valueOf(totalExpense));
        binding.investmentValueText.setText(String.valueOf(totalInvestment));

        binding.expenseValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        binding.investmentValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        binding.expenseLabelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        binding.investmentLabelText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    private void checkStoragePermissionAndDownload() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            downloadInvestmentReport();
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                downloadInvestmentReport();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void downloadInvestmentReport() {
        List<InvestmentModel> investments = dbHelper.getAllInvestmentsForPDF(loggedInUserId,startDate,endDate);

        if (investments == null || investments.isEmpty()) {
            Toast.makeText(getContext(), "No investment data available.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalExpense = dbHelper.getTotalExpense(loggedInUserId,startDate,endDate);
        double totalInvestment = dbHelper.getTotalInvestment(loggedInUserId,startDate,endDate);

        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File moneyBookFolder = new File(downloadsFolder, "MoneyBook");

        if (!moneyBookFolder.exists() && !moneyBookFolder.mkdirs()) {
            Toast.makeText(getContext(), "Failed to create MoneyBook folder.", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String fileName = "InvestmentReport_" + timeStamp + ".pdf";
        File pdfFile = new File(moneyBookFolder, fileName);

        generatePdfAndSave(pdfFile, investments, totalExpense, totalInvestment);
    }

    private void generatePdfAndSave(File pdfFile, List<InvestmentModel> investments, double totalExpense, double totalInvestment) {
        PdfDocument pdfDocument = new PdfDocument();

        int pageWidth = 800;
        int pageHeight = 600;
        int rowsPerPage = 25;
        int yPosition = 120;
        int currentPage = 1;

        Paint paint = new Paint();

        try {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPage).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            paint.setColor(Color.BLACK);
            paint.setTextSize(14);

            drawTotalsAtTop(canvas, paint, totalExpense, totalInvestment);

            drawTableHeaders(canvas, paint);

            for (int i = 0; i < investments.size(); i += rowsPerPage) {
                if (i > 0) {
                    pdfDocument.finishPage(page);
                    currentPage++;
                    pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPage).create();
                    page = pdfDocument.startPage(pageInfo);
                    canvas = page.getCanvas();
                }

                int startIndex = i;
                int endIndex = Math.min(i + rowsPerPage, investments.size());
                drawInvestmentsOnPage(canvas, paint, investments.subList(startIndex, endIndex), yPosition);
            }

            pdfDocument.finishPage(page);

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

    private void drawTotalsAtTop(Canvas canvas, Paint paint, double totalExpense, double totalInvestment) {
        int yPosition = 30;
        paint.setTextSize(18);

        canvas.drawText("Total Expense: " + totalExpense, 10, yPosition, paint);
        yPosition += 20;
        canvas.drawText("Total Investment: " + totalInvestment, 10, yPosition, paint);
    }

    private void drawTableHeaders(Canvas canvas, Paint paint) {
        canvas.drawText("ID", 10, 100, paint);
        canvas.drawText("Amount", 50, 100, paint);
        canvas.drawText("Date", 160, 100, paint);
        canvas.drawText("Payment Mode",350,100,paint);
        canvas.drawText("Description", 480, 100, paint);
    }

    private void drawInvestmentsOnPage(Canvas canvas, Paint paint, List<InvestmentModel> investments, int initialYPosition) {
        int yPosition = initialYPosition;

        int idCount = 1;

        for (InvestmentModel investment : investments) {
            canvas.drawText(String.valueOf(idCount), 10, yPosition, paint);
            canvas.drawText(String.valueOf(investment.getInvestmentAmount()), 50, yPosition, paint);
            canvas.drawText(investment.getInvestmentDate(), 160, yPosition, paint);
            canvas.drawText(investment.getPaymentModeName(), 350, yPosition, paint);
            canvas.drawText(investment.getInvestmentDescription(), 480, yPosition, paint);
            yPosition += 20;
            idCount++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
