package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.Repository.RequestRepository;

import com.example.demo.model.Request;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
     @GetMapping("/get")
    public List<Request> getAllRequest() {
        return requestRepository.findAll();
    }

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/create")
    public ResponseEntity<Request> createComplaint(@RequestBody Request request) {
        Request savedRequest = requestRepository.save(request);
        return ResponseEntity.ok(savedRequest);
    }
}

