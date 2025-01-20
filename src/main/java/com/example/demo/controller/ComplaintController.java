package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
    @GetMapping
    public String getAllcomplaint() {
        return "List of all complaint";
    }

    @PostMapping
    public String createcomplaint() {
        return "Bill created successfully";
    }
}
