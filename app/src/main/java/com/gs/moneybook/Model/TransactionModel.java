package com.gs.moneybook.Model;

public class TransactionModel {

    private int transactionId;
    private double amount;
    private String date; // Could be a String or Date type depending on usage
    private int categoryId;
    private int userId;
    private String type; // "Income" or "Expense"
    private int paymentModeId;
    private String categoryName; // Add this for the category name


    public TransactionModel(int transactionId, double amount, String date, int categoryId, int userId, String type, int paymentModeId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.userId = userId;
        this.type = type;
        this.paymentModeId = paymentModeId;

    }

    public TransactionModel(int transactionId, double amount, String date, int categoryId, String type, int paymentModeId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.type = type;
        this.paymentModeId = paymentModeId;
    }

    public TransactionModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(int paymentModeId) {
        this.paymentModeId = paymentModeId;
    }
}

