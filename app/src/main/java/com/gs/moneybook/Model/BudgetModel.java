package com.gs.moneybook.Model;

public class BudgetModel {

    private int id;
    private double amount;
    private String startDate;
    private String endDate;
    private String category;
    private String budgetType; // Monthly, Yearly, etc.

    public BudgetModel(int id, double amount, String startDate, String endDate, String category, String budgetType) {
        this.id = id;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.budgetType = budgetType;
    }

    public BudgetModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }
}
