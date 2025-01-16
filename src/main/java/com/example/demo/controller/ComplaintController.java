package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bills")
public class ComplaintController {
    @GetMapping
    public String getAllBills() {
        return "List of all bills";
    }

    @PostMapping
    public String createBill() {
        return "Bill created successfully";
    }
}
