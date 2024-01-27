package com.dobiasdev.spa.services;

import com.dobiasdev.spa.persistance.entities.User;
import com.dobiasdev.spa.model.dtos.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();

    User getUserById(Long id);

    long createUser(UserDto user);

    void deleteUserById(Long id);

    long updateUser(UserDto user);

    void updateEntityFromDto(User dbUser, UserDto dtoUser);

    Optional<User> findUserByEmail(String email);

    Optional<User> findByEmailOrUserName(String username);
}
