package com.example.demo.Repository;

import com.example.demo.model.Generate;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenerateRepository extends JpaRepository<Generate, Long> {

    List<Generate> findByUser(User user);

    // For optional: check if control number already exists for user
    Optional<Generate> findTopByUser(User user);
}
