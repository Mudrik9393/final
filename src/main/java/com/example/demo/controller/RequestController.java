package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    @GetMapping
    public String getAllRequests() {
        return "List of all requests";
    }

    @PostMapping
    public String createRequest() {
        return "Request created successfully";
    }
}
