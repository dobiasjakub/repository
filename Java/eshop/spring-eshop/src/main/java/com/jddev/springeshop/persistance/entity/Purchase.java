package com.jddev.springeshop.persistance.entity;

import com.jddev.springeshop.persistance.enums.DeviceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="PURCHASES")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @Column(name = "DEVICE_ID", nullable = false)
    private Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private Device device;

    @Column(name = "PRICE" , nullable = false)
    private Double price;

    @Column(name = "COLOR" , nullable = false)
    private Double color;

    @Column(name = "CREATED", nullable = false, length = 100)
    private LocalDateTime created;

    @Column(name = "PURCHASE_TIME", nullable = false, length = 100)
    private LocalDateTime purchaseTime;

}
