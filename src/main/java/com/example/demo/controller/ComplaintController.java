package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.ComplaintRepository;

import com.example.demo.model.Complaint;


@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
     @GetMapping("/get")
    public List<Complaint> getAllComplaint() {
        return complaintRepository.findAll();
    }

    

    @Autowired
    private ComplaintRepository complaintRepository;

    @PostMapping("/create")
    public ResponseEntity<Complaint> createComplaint(@RequestBody Complaint complaint) {
        Complaint savedComplaint = complaintRepository.save(complaint); 
        return ResponseEntity.ok(savedComplaint);
    }
}
