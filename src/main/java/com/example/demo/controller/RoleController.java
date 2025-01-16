package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")

public class RoleController {
    @GetMapping
    public String getAllRoles() {
        return "List of all roles";
    }

    @PostMapping
    public String createRole() {
        return "Role created successfully";
    }
}
