package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.StreetRepository;

import com.example.demo.model.Street;

@RestController
@RequestMapping("/api/streets")
public class StreetController { 
    @GetMapping("/get")
    public List<Street> getAllStreet() {
        return streetRepository.findAll();
    }

    @Autowired
    private StreetRepository streetRepository;

    @PostMapping("/create")
    public ResponseEntity<Street> createComplaint(@RequestBody Street street) {
        Street savedStreet = streetRepository.save(street); 
        return ResponseEntity.ok(savedStreet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStreet(@PathVariable Long id) {
        streetRepository.deleteById(id);
        return new ResponseEntity<>("Delete data success", HttpStatus.OK);
    }
}

