package com.dobiasdev.spa.services.impl;

import com.dobiasdev.spa.persistance.entities.User;
import com.dobiasdev.spa.model.dtos.UserDto;
import com.dobiasdev.spa.repositories.UserRepository;
import com.dobiasdev.spa.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public long createUser(UserDto user) {
        var dbUser = new User();

        dbUser.setCreated(LocalDateTime.now());

        updateEntityFromDto(dbUser, user);
        userRepository.save(dbUser);

        return 1;
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public long updateUser(UserDto user) {
        var dbUser = userRepository.findById(user.getId()).orElseThrow();

        updateEntityFromDto(dbUser, user);
        userRepository.save(dbUser);

        return dbUser.getId();
    }

    @Override
    public void updateEntityFromDto(User dbUser, UserDto dtoUser) {
        dbUser.setUsername(dtoUser.getUsername());
        dbUser.setEmail(dtoUser.getEmail());
        dbUser.setPassword(dtoUser.getPassword());
        dbUser.setModified(LocalDateTime.now());

        userRepository.save(dbUser);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailOrUserName(String username) {
        return userRepository.findByEmailOrUserName(username);
    }

}
