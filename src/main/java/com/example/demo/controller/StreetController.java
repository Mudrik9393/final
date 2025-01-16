package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/streets")
public class StreetController {
    @GetMapping
    public String getAllStreets() {
        return "List of all streets";
    }

    @PostMapping
    public String createStreet() {
        return "Street created successfully";
    }
}
