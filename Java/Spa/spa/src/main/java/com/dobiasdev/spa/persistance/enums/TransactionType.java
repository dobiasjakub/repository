package com.dobiasdev.spa.persistance.enums;


public enum TransactionType implements PersistentEnum, LocalizedEnum {
    Income(1, "Příchozí platba"),
    Outcome(2, "Odchozí platba");

    private final int value;
    private final String czechText;

    TransactionType(int value, String czechText) {
        this.value = value;
        this.czechText = czechText;
    }

    @Override
    public String getCzechText() {
        return czechText;
    }

    @Override
    public int getId() {
        return value;
    }
}
