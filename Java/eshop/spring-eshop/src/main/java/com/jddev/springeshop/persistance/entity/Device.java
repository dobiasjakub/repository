package com.jddev.springeshop.persistance.entity;

import com.jddev.springeshop.persistance.enums.DeviceType;
import com.jddev.springeshop.persistance.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "DEVICES")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "PRICE", nullable = false, length = 100)
    private double price;

    @Column(name = "CREATED", nullable = false, length = 100)
    private LocalDateTime created;

    @Column(name = "MODIFIED", nullable = false, length = 100)
    private LocalDateTime modified;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVICE_TYPE", nullable = false, length = 100)
    private DeviceType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    private List<Purchase> purchaseList;

}
