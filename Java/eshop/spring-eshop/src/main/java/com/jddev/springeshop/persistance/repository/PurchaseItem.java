package com.jddev.springeshop.persistance.repository;

import com.jddev.springeshop.persistance.enums.DeviceType;

import java.time.LocalDateTime;

public interface PurchaseItem {
    String getName();
    DeviceType getType();
    Double getPrice();
    String getColor();
    LocalDateTime getPurchaseItem();
}
