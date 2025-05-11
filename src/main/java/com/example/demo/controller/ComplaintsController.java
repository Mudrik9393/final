package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Services.ComplaintsService;
import com.example.demo.model.Complaints;

@CrossOrigin
@RestController
@RequestMapping("/api/complaints")
public class ComplaintsController {

    private final ComplaintsService complaintsService;

    public ComplaintsController(ComplaintsService complaintsService) {
        this.complaintsService = complaintsService;
    }

    @GetMapping("/get")
    public List<Complaints> getAllComplaints() {
        return complaintsService.getAllComplaints();
    }

    @PostMapping("/create")
    public Complaints createComplaints(@RequestBody Complaints complaints) {
        return complaintsService.createComplaint(complaints);
    }
}
