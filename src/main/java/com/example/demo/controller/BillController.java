package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.BillRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Bill;
import com.example.demo.model.User;

@CrossOrigin 
@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository;

    @GetMapping("/get")
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable Long userId) {
        List<Bill> bills = billRepository.findByUserUserId(userId);
        if (bills.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bills);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<Bill> createBill(@PathVariable Long userId, @RequestBody Bill bill) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (bill.getUnit() == null || bill.getUnit() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        bill.setUser(user);
        bill.setUnit(bill.getUnit());

        Bill savedBill = billRepository.save(bill);
        return ResponseEntity.ok(savedBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        billRepository.deleteById(id);
        return new ResponseEntity<>("Delete data success", HttpStatus.OK);
    }
}
