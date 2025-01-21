package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.RoleRepository;
import com.example.demo.model.Bill;
import com.example.demo.model.Role;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    @GetMapping("/get")
    public List<Role> getAllBills() {
        return billRepository.findAll();
    }

    

    @Autowired
    private RoleRepository billRepository;

    @PostMapping("/create")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill savedBill = billRepository.save(bill); 
        return ResponseEntity.ok(savedBill);
    }
}
