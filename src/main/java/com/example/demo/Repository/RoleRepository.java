package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Bill;
import com.example.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Bill save(Bill bill);
}  

