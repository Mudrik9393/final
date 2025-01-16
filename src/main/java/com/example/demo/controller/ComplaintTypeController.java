package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/complaint-types")
public class ComplaintTypeController {
    @GetMapping
    public String getAllComplaintTypes() {
        return "List of all complaint types";
    }

    @PostMapping
    public String createComplaintType() {
        return "Complaint type created successfully";
    }
}
