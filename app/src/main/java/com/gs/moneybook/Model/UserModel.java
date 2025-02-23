package com.gs.moneybook.Model;

public class UserModel {
    private int id;
    private String fullName;
    private String mobileNumber;
    private String email;
    private String occupation;
    private String password;
    private String dob;
    private double monthlyIncome;
    private double savingsGoal;
    private String currency;

    public UserModel(int id, String fullName, String mobileNumber, String email, String occupation, String password, String dob, double monthlyIncome, double savingsGoal, String currency) {
        this.id = id;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.occupation = occupation;
        this.password = password;
        this.dob = dob;
        this.monthlyIncome = monthlyIncome;
        this.savingsGoal = savingsGoal;
        this.currency = currency;
    }

    public UserModel() {
    }

    public UserModel(String fullName, String mobileNumber, String email, String occupation, String password, String dob, double monthlyIncome, double savingsGoal, String currency) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.occupation = occupation;
        this.password = password;
        this.dob = dob;
        this.monthlyIncome = monthlyIncome;
        this.savingsGoal = savingsGoal;
        this.currency = currency;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public double getSavingsGoal() {
        return savingsGoal;
    }

    public void setSavingsGoal(double savingsGoal) {
        this.savingsGoal = savingsGoal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
