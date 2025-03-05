package com.gs.moneybook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gs.moneybook.Model.InvestmentModel;
import com.gs.moneybook.R;

import java.util.ArrayList;
import java.util.List;

public class InvestmentAdapter extends RecyclerView.Adapter<InvestmentAdapter.InvestmentViewHolder>{

    private Context context;
    private List<InvestmentModel> investmentModelList;

    public InvestmentAdapter(Context context, List<InvestmentModel> investmentModelList){

        this.context = context;
        this.investmentModelList = investmentModelList;

    }


    @NonNull
    @Override
    public InvestmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_history_item,parent,false);


        return new InvestmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvestmentViewHolder holder, int position) {

        InvestmentModel investmentModel = investmentModelList.get(position);

        holder.InvestmentDate.setText(investmentModel.getInvestmentDate());
        double investmentAmount = investmentModel.getInvestmentAmount();
        holder.InvestmentAmount.setText(String.format("%.2f",investmentAmount));
        holder.InvestmentPaymentMode.setText(investmentModel.getPaymentModeName());
        holder.InvestmentDescription.setText(investmentModel.getInvestmentDescription());
    }

    @Override
    public int getItemCount() {
        return investmentModelList.size();
    }

    public static class InvestmentViewHolder extends RecyclerView.ViewHolder{

        TextView InvestmentAmount, InvestmentDate, InvestmentPaymentMode, InvestmentDescription;

        public InvestmentViewHolder(@NonNull View itemView) {
            super(itemView);

            InvestmentAmount = itemView.findViewById(R.id.tvInvestmentAmount);
            InvestmentDate = itemView.findViewById(R.id.tvInvestmentDate);
            InvestmentPaymentMode = itemView.findViewById(R.id.tvInvestmentPaymentMode);
            InvestmentDescription = itemView.findViewById(R.id.tvInvestmentDescription);
        }
    }

}
