package com.gs.moneybook.Model;

public class TransactionModel {

    private int id;
    private String transactionName;
    private String category;
    private double amount;
    private String date;
    private String transactionType; // Income or Expense

    public TransactionModel(int id, String transactionName, String category, double amount, String date, String transactionType) {
        this.id = id;
        this.transactionName = transactionName;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;
    }

    public TransactionModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
