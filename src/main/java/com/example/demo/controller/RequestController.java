package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Request;
import com.example.demo.Repository.RequestRepository;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin
public class RequestController {

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createRequest(
            @RequestParam("requestName") String requestName,
            @RequestParam("fullName") String fullName,
            @RequestParam("address") String address,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("date") String dateStr,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "document", required = false) MultipartFile documentFile) {

        try {
            Request request = new Request();
            request.setRequestName(requestName);
            request.setFullName(fullName);
            request.setAddress(address);
            request.setPhoneNumber(phoneNumber);
            request.setDate(LocalDate.parse(dateStr));
            request.setMessage(message);
            request.setLatitude(latitude);
            request.setLongitude(longitude);
            request.setAccountNumber("ACC" + UUID.randomUUID().toString().substring(0, 8));

            // File handling
            if (documentFile != null && !documentFile.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads";
                String fileName = UUID.randomUUID().toString() + "_" + documentFile.getOriginalFilename();
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }

                java.nio.file.Path filePath = uploadPath.resolve(fileName);
                documentFile.transferTo(filePath.toFile());

                // Save only file name, not full path
                request.setDocument(fileName);
            } else {
                request.setDocument(null);
            }

            Request saved = requestRepository.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
        if (!requestRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
        requestRepository.deleteById(id);
        return ResponseEntity.ok("Delete success");
    }

    // ✅ Endpoint mpya ya request locations
    @GetMapping("/locations")
    public List<RequestLocationDTO> getRequestLocations() {
        return requestRepository.findAll().stream()
            .filter(r -> r.getLatitude() != null && r.getLongitude() != null)
            .map(r -> new RequestLocationDTO(
                r.getLatitude(),
                r.getLongitude(),
                r.getRequestName()
            ))
            .toList();
    }

    // ✅ DTO class kwa frontend (Map)
    public static class RequestLocationDTO {
        private double latitude;
        private double longitude;
        private String requestName;

        public RequestLocationDTO(double latitude, double longitude, String requestName) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.requestName = requestName;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getRequestName() {
            return requestName;
        }
    }
}
