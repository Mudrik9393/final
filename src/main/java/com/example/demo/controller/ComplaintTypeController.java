package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.ComplaintTypeRepository;

import com.example.demo.model.ComplaintType;
import com.example.demo.model.Role;

@RestController
@RequestMapping("/api/complaint-types")
public class ComplaintTypeController {
     @GetMapping("/get")
    public List<ComplaintType> getAllComplaintTypes() {
        return complaintTypeRepository.findAll();
    }

    

    @Autowired
    private ComplaintTypeRepository complaintTypeRepository;

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody ComplaintTypeRepository complaintTypeRepository) {
        Role savedComplaintTypeRepository = complaintTypeRepository.save(complaintTypeRepository); 
        return ResponseEntity.ok(savedComplaintTypeRepository);
    }
}
