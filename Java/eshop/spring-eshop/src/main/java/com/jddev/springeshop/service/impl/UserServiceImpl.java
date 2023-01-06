package com.jddev.springeshop.service.impl;

import com.jddev.springeshop.persistance.entity.User;
import com.jddev.springeshop.persistance.enums.Role;
import com.jddev.springeshop.persistance.repository.UserRepository;
import com.jddev.springeshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setCreated(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void changeUserRole(Role newRole, String username) {
        userRepository.updateUserRole(username, newRole);
    }
}

