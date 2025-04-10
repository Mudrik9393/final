package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Request;

public interface RequestRepository extends JpaRepository<Request, String> {
}
