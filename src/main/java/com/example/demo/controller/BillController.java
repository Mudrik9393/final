package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.BillRepository;
import com.example.demo.model.Bill;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private static final double unitprice = 500.0; // Fixed price per unit

    @Autowired
    private BillRepository billRepository;

    @GetMapping("/get")
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        float fixedUnitPrice = 500f; 
        bill.setUnitprice(fixedUnitPrice); 
        
    
        Bill savedBill = billRepository.save(bill);
        return ResponseEntity.ok(savedBill);
    }
    
     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        billRepository.deleteById(id);
        return new ResponseEntity<>("Delete data success", HttpStatus.OK);
    }
}
