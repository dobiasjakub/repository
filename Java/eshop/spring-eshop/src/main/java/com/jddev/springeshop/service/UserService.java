package com.jddev.springeshop.service;

import com.jddev.springeshop.persistance.entity.User;
import com.jddev.springeshop.persistance.enums.Role;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    Optional<User> findByUsername(String username);

    void changeUserRole(Role newRole, String username);
}
