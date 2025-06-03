package com.example.demo.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.ComplaintsRepository;

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

    @Autowired
    private ComplaintsRepository complaintsRepository;

    @PostMapping("/create")
    public Complaints createComplaints(@RequestBody Complaints complaints) {
        return complaintsService.createComplaint(complaints);
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaints(@PathVariable Long id) {
        complaintsRepository.deleteById(id);
        return new ResponseEntity<>("Delete data success", HttpStatus.OK);
    }

     @PutMapping("/{id}")
public ResponseEntity<Complaints> updateComplaint(@PathVariable Long id, @RequestBody Complaints updatedComplaint) {
    return complaintsRepository.findById(id)
        .map(complaint -> {
            complaint.setFullName(updatedComplaint.getFullName());
            complaint.setComplaintName(updatedComplaint.getComplaintName());
            complaint.setAccountNumber(updatedComplaint.getAccountNumber());
            complaint.setStreet(updatedComplaint.getStreet());
            complaint.setDistrict(updatedComplaint.getDistrict());
            complaint.setPhoneNumber(updatedComplaint.getPhoneNumber());
            Complaints savedComplaint = complaintsRepository.save(complaint);
            return ResponseEntity.ok(savedComplaint);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
}

}
