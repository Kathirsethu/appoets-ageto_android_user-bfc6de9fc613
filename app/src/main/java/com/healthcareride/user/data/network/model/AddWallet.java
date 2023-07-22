package com.healthcareride.user.data.network.model;

import com.google.gson.annotations.SerializedName;

public class AddWallet {

    private String success;
    private String message;
    @SerializedName("wallet_balance")
    private double balance;

    public AddWallet() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
