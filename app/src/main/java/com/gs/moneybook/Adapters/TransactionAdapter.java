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
        // Set the transaction date and time
        holder.transactionDate.setText(transactionModel.getDate());  // Date field
        holder.transactionTime.setText(getTimeFromDate(transactionModel.getDate())); // Assume this method formats time

        // Set the category name (You'll have to resolve the category ID to the actual name)
        holder.categoryName.setText((transactionModel.getCategoryName()));  // Replace with actual category fetching

        // Set the transaction amount and change color based on the type (Income/Expense)
        double amount = transactionModel.getAmount();
        if (transactionModel.getType().equals("Expense")) {
            holder.transactionAmount.setText("-₹" + amount);
            holder.transactionAmount.setTextColor(Color.RED);  // Red for Expense
        } else {
            holder.transactionAmount.setText("+₹" + amount);
            holder.transactionAmount.setTextColor(Color.GREEN);  // Green for Income
        }




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
