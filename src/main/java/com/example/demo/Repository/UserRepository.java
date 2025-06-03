package com.example.demo.Repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Method to find user by email
    Optional<User> findByEmail(String email);

    // Method to check if an email is already registered
    boolean existsByEmail(String email);

    List<User> findByRole_RoleName(String roleName);
}
