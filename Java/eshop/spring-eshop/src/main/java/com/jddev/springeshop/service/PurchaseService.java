package com.jddev.springeshop.service;

import com.jddev.springeshop.persistance.entity.Purchase;
import com.jddev.springeshop.persistance.repository.PurchaseItem;

import java.util.List;

public interface PurchaseService {
    Purchase savePurchase(Purchase purchase);

    List<PurchaseItem> findPurchaseItemsOfUser(Long userId);
}
