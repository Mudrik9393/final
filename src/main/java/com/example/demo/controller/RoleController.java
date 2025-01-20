package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.RoleRepository;
import com.example.demo.model.Role;

@RestController
@RequestMapping("/api/roles")

public class RoleController {
    @GetMapping("/get")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role savedRole = roleRepository.save(role); // Save role to the database
        return ResponseEntity.ok(savedRole);
    }
}
