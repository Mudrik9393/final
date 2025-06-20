package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.BillRepository;
import com.example.demo.model.Bill;
import com.example.demo.model.User;
import com.example.demo.Repository.UserRepository;


@RestController
@RequestMapping("/api/bills")
public class BillController {

    private static final double unitprice = 500.0; // Fixed price per unit

    @Autowired
  private UserRepository userRepository;


    @Autowired
    private BillRepository billRepository;

    @GetMapping("/get")
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @PostMapping("/create/{userId}")
public ResponseEntity<Bill> createBill(@PathVariable Long userId, @RequestBody Bill bill) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    bill.setUser(user);
    bill.setUnitprice(500f); // mfano wa bei ya unit

    Bill savedBill = billRepository.save(bill);
    return ResponseEntity.ok(savedBill);
}

    
     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        billRepository.deleteById(id);
        return new ResponseEntity<>("Delete data success", HttpStatus.OK);
    }
}
