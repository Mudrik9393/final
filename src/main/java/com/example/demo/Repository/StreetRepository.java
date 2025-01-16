package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Street;

public interface StreetRepository extends JpaRepository<Street, Long> {
}
