package com.gs.moneybook.Model;

public class PaymentModeModel {
    private int paymentModeId;
    private String paymentModeName;
    private int userId;

    public PaymentModeModel(int paymentModeId, String paymentModeName, int userId) {
        this.paymentModeId = paymentModeId;
        this.paymentModeName = paymentModeName;
        this.userId = userId;
    }


    public PaymentModeModel() {
    }

    public int getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(int paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
