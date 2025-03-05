package com.gs.moneybook.Model;

public class InvestmentModel {

    private double investmentAmount;
    private String investmentDate;
    private String paymentModeName; // Added payment mode name
    private String investmentDescription;


    public InvestmentModel() {
    }

    public InvestmentModel(double investmentAmount, String investmentDate, String paymentModeName, String investmentDescription) {
        this.investmentAmount = investmentAmount;
        this.investmentDate = investmentDate;
        this.paymentModeName = paymentModeName;
        this.investmentDescription = investmentDescription;
    }

    public double getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(double investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(String investmentDate) {
        this.investmentDate = investmentDate;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public String getInvestmentDescription() {
        return investmentDescription;
    }

    public void setInvestmentDescription(String investmentDescription) {
        this.investmentDescription = investmentDescription;
    }


}
