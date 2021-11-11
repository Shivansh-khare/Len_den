package com.example.lenden.models;

public class Transaction_record {
    String sendId;
    Double amount;
    boolean isRec;
    String note;

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isRec() {
        return isRec;
    }

    public void setRec(boolean rec) {
        isRec = rec;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Transaction_record(String sendId, Double amount, boolean isRec, String note) {
        this.sendId = sendId;
        this.amount = amount;
        this.isRec = isRec;
        this.note = note;
    }
}
