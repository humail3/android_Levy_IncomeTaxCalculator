package com.novahumail.levy;

public class TaxDataModel {
    private int totalPayableTax;
    private String todayDate;

    public TaxDataModel() {
        // Required empty constructor for Firestore
    }

    public TaxDataModel(int totalPayableTax, String todayDate) {
        this.totalPayableTax = totalPayableTax;
        this.todayDate = todayDate;
    }

    public int getTotalPayableTax() {
        return totalPayableTax;
    }

    public void setTotalPayableTax(int totalPayableTax) {
        this.totalPayableTax = totalPayableTax;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }
}
