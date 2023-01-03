package com.jddev.springeshop.persistance.repository;

import com.jddev.springeshop.persistance.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>  {
    @Query("select "+
            "d.name as name, d.type as type, p.price as price, p.color as color, p.purchaseTime as purchaseTime"+
            " from Purchase p left join Device d on d.id = p.deviceId where p.userId = :userId")
    List<PurchaseItem> findAllByPurchasesOfUser(@Param("userId") Long id);
}
