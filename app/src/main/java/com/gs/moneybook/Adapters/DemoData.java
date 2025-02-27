package com.gs.moneybook.Adapters;

import com.gs.moneybook.Model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class DemoData {

    public static List<TransactionModel> getDemoTransactions() {
        List<TransactionModel> transactions = new ArrayList<>();

        transactions.add(new TransactionModel( 1,50.00, "2025-02-25 10:30:00", "Expense", "Groceries"));
        transactions.add(new TransactionModel( 1,5000.00, "2025-02-24 14:15:00", "Income", "Salary"));
        transactions.add(new TransactionModel( 1,120.00, "2025-02-23 18:45:00", "Expense", "Restaurant"));
        transactions.add(new TransactionModel( 1,300.00, "2025-02-22 20:10:00", "Expense", "Electricity Bill"));
        transactions.add(new TransactionModel(1, 2000.00, "2025-02-21 09:00:00", "Income", "Freelancing"));

        return transactions;
    }

}
