package com.dobiasdev.spa.persistance.enums;

public enum Role implements PersistentEnum, LocalizedEnum{

    Admin(1, "Admin"),
    User(2, "Uživatel");

    private final int value;
    private final String czechText;

    Role(int value, String czechText) {
        this.value = value;
        this.czechText = czechText;
    }

    @Override
    public String getCzechText() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }
}
