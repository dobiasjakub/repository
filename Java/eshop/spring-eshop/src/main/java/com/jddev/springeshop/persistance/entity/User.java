package com.jddev.springeshop.persistance.entity;

import com.jddev.springeshop.persistance.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "CREATED", nullable = false, length = 100)
    private LocalDateTime created;

    @Column(name = "MODIFIED", length = 100)
    private LocalDateTime modified;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false, length = 100)
    private Role role;

    @Transient
    private String token;
}
