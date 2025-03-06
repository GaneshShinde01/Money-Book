package com.gs.moneybook.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
        Log.d("TransactionAdapter", "Binding transaction at position: " + position + ", Transaction: " + transactionModel.toString());


        // Safely parse and format the date using DateUtils
        String date = transactionModel.getTransactionDate();
        if (date != null && !date.isEmpty()) {
            String[] dateTimeParts = date.split(" ");
            if (dateTimeParts.length >= 2) {
                holder.transactionDate.setText(dateTimeParts[0]); // Directly set the date part
                holder.transactionTime.setText(dateTimeParts[1]); // Directly set the time part
            } else {
                holder.transactionDate.setText(date); // If no space, set the whole string as date
                holder.transactionTime.setText(""); // Time is empty
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
            holder.transactionAmount.setText("-₹" + String.format("%.2f", amount)); // **Highlighted: Formatted the amount to 2 decimal places**
            holder.transactionAmount.setTextColor(context.getResources().getColor(R.color.expense_red));
        } else {
            holder.transactionAmount.setText("+₹" + String.format("%.2f", amount)); // **Highlighted: Formatted the amount to 2 decimal places**
            holder.transactionAmount.setTextColor(context.getResources().getColor(R.color.income_green));
        }

    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{

        TextView transactionDate, transactionTime, categoryName, transactionAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryNameTitem);
            transactionDate = itemView.findViewById(R.id.transactionDateTitem);
            transactionTime = itemView.findViewById(R.id.transactionTimeTitem);
            transactionAmount = itemView.findViewById(R.id.transactionAmountTitem);

        }
    }


/*
    public void updateData(List<TransactionModel> newTransactions) {
        this.transactionModelList.clear();
        this.transactionModelList.addAll(newTransactions);
        notifyDataSetChanged();
    }
*/

}
