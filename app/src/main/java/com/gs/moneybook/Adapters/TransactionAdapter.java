package com.gs.moneybook.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gs.moneybook.Model.TransactionModel;
import com.gs.moneybook.R;
import com.gs.moneybook.Utilities.DateUtils;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionModel> transactionModelList;
    private Context context;


    public TransactionAdapter(Context context, List<TransactionModel> transactionModelList){
        this.context = context;
        this.transactionModelList = transactionModelList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item,parent,false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        TransactionModel transactionModel = transactionModelList.get(position);

        // Safely parse and format the date using DateUtils
        String date = transactionModel.getTransactionDate();
        if (date != null && !date.isEmpty()) {
            String[] dateTimeParts = date.split(" ");
            if (dateTimeParts.length >= 2) {
                // Format date as "dd/MM/yyyy" using DateUtils
                String[] dateParts = dateTimeParts[0].split("-");
                if (dateParts.length == 3) {
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1;  // Month is 0-based in Calendar
                    int day = Integer.parseInt(dateParts[2]);
                    holder.transactionDate.setText(DateUtils.formatDate(day, month, year));  // Format the date
                } else {
                    holder.transactionDate.setText(date);  // Fallback to raw date if format is wrong
                }
                holder.transactionTime.setText(dateTimeParts[1]);  // Set time part
            } else {
                holder.transactionDate.setText(date);  // Fallback to entire date if format is wrong
                holder.transactionTime.setText("");   // Clear time in case of incorrect format
            }
        } else {
            holder.transactionDate.setText("N/A");
            holder.transactionTime.setText("");
        }

        // Set the category name
        holder.categoryName.setText(transactionModel.getCategoryName() != null ? transactionModel.getCategoryName() : "Unknown");

        // Set transaction amount and color based on type
        double amount = transactionModel.getTransactionAmount();
        if ("Expense".equals(transactionModel.getTransactionType())) {
            holder.transactionAmount.setText("-₹" + amount);
            holder.transactionAmount.setTextColor(context.getResources().getColor(R.color.expense_red));  // Use resources for colors
        } else {
            holder.transactionAmount.setText("+₹" + amount);
            holder.transactionAmount.setTextColor(context.getResources().getColor(R.color.income_green));  // Use resources for colors
        }

        // Comment out the category icon for now
        // holder.categoryIcon.setImageResource(R.drawable.check);
    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryIcon;
        TextView transactionDate, transactionTime, categoryName, transactionAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryNameTitem);
            transactionDate = itemView.findViewById(R.id.transactionDateTitem);
            transactionTime = itemView.findViewById(R.id.transactionTimeTitem);
            transactionAmount = itemView.findViewById(R.id.transactionAmountTitem);



        }
    }

    // Helper method to extract time from the date string (assuming date is in "YYYY-MM-DD HH:MM:SS" format)
    private String getTimeFromDate(String date) {
        // Parse and return the time part
        return date.split(" ")[1];  // Assuming date is "YYYY-MM-DD HH:MM:SS", return the time part
    }

}
