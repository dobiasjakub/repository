package com.jddev.springeshop.persistance.repository;

import com.jddev.springeshop.persistance.entity.User;
import com.jddev.springeshop.persistance.enums.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {

    User findByUsername(String username);

    @Query("update User set role = :role where username = :username")
    void updateUserRole(@Param("username") String username, @Param("role") Role role);
}
