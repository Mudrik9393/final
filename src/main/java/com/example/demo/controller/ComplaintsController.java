package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.ComplaintsRepository;
import com.example.demo.Services.ComplaintsService;
import com.example.demo.model.Complaints;

@CrossOrigin
@RestController
@RequestMapping("/api/complaints")
public class ComplaintsController {

    private final ComplaintsService complaintsService;

    @Autowired
    private ComplaintsRepository complaintsRepository;

    public ComplaintsController(ComplaintsService complaintsService) {
        this.complaintsService = complaintsService;
    }

    // Existing endpoints (UNCHANGED)
    @GetMapping("/get")
    public List<Complaints> getAllComplaints() {
        return complaintsService.getAllComplaints();
    }

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

                // ✅ Location info
                complaint.setLatitude(updatedComplaint.getLatitude());
                complaint.setLongitude(updatedComplaint.getLongitude());

                Complaints savedComplaint = complaintsRepository.save(complaint);
                return ResponseEntity.ok(savedComplaint);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ NEW: Return only location data (for the map)
    @GetMapping("/locations")
    public List<ComplaintLocationDTO> getComplaintLocations() {
        return complaintsRepository.findAll().stream()
            .filter(c -> c.getLatitude() != null && c.getLongitude() != null)
            .map(c -> new ComplaintLocationDTO(
                c.getLatitude(),
                c.getLongitude(),
                c.getComplaintName()
            ))
            .collect(Collectors.toList());
    }

    // ✅ DTO for frontend (Map)
    public static class ComplaintLocationDTO {
        private double latitude;
        private double longitude;
        private String complaintName;

        public ComplaintLocationDTO(double latitude, double longitude, String complaintName) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.complaintName = complaintName;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getComplaintName() {
            return complaintName;
        }
    }
}
