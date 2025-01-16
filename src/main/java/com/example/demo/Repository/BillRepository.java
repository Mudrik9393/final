package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
