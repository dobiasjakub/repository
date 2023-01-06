package com.jddev.springeshop.service.impl;

import com.jddev.springeshop.persistance.entity.Purchase;
import com.jddev.springeshop.persistance.repository.PurchaseItem;
import com.jddev.springeshop.persistance.repository.PurchaseRepository;
import com.jddev.springeshop.service.PurchaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase savePurchase(Purchase purchase) {
        purchase.setPurchaseTime(LocalDateTime.now());

        return purchaseRepository.save(purchase);
    }

    @Override
    public List<PurchaseItem> findPurchaseItemsOfUser(Long userId) {
        return purchaseRepository.findAllByPurchasesOfUser(userId);
    }
}
