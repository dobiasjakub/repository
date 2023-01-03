package com.jddev.springeshop.service;

import com.jddev.springeshop.persistance.entity.User;
import com.jddev.springeshop.persistance.enums.Role;

public interface UserService {
    User saveUser(User user);

    User findByUsername(String username);

    void changeUserRole(Role newRole, String username);
}
