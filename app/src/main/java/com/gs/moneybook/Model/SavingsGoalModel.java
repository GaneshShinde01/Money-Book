package com.gs.moneybook.Model;

public class SavingsGoalModel {

    private int id;
    private double targetAmount;
    private double currentAmount;
    private String goalName;
    private String goalDeadline;
    private String status; // E.g., In Progress, Achieved

    public SavingsGoalModel(int id, double targetAmount, double currentAmount, String goalName, String goalDeadline, String status) {
        this.id = id;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.goalName = goalName;
        this.goalDeadline = goalDeadline;
        this.status = status;
    }

    public SavingsGoalModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalDeadline() {
        return goalDeadline;
    }

    public void setGoalDeadline(String goalDeadline) {
        this.goalDeadline = goalDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
