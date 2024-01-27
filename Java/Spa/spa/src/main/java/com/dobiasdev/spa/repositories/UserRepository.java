package com.dobiasdev.spa.repositories;

import com.dobiasdev.spa.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.email = :username OR u.username = :username")
    Optional<User> findByEmailOrUserName(String username);

    boolean existsByEmail(String email);

}
