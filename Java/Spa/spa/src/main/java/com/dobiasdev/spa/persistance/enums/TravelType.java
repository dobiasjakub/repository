package com.dobiasdev.spa.persistance.enums;


public enum TravelType implements PersistentEnum, LocalizedEnum {
    Car(1, "Auto"),
    Train(2, "Vlak"),
    Bus(3, "Autobus");

    private final int value;
    private final String czechText;

    TravelType(int value, String czechText) {
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
