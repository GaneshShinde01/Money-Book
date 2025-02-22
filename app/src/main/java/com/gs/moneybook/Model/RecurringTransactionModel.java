package com.gs.moneybook.Model;

public class RecurringTransactionModel {

    private int id;
    private String transactionName;
    private double amount;
    private String transactionDate; // Date of recurring transaction
    private String frequency; // E.g., Weekly, Monthly, Yearly
    private String category;
    private String transactionType; // Income or Expense

    public RecurringTransactionModel(int id, String transactionName, double amount, String transactionDate, String frequency, String category, String transactionType) {
        this.id = id;
        this.transactionName = transactionName;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.frequency = frequency;
        this.category = category;
        this.transactionType = transactionType;
    }

    public RecurringTransactionModel() {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
