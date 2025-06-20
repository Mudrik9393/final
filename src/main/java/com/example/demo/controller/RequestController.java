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
    public ResponseEntity<Request> createRequest(
            @ModelAttribute Request request,
            @RequestParam(value = "document", required = false) MultipartFile documentFile) {

        // Generate account number automatically
        request.setAccountNumber("ACC" + UUID.randomUUID().toString().substring(0, 8));

        // Set date to today if not provided
        if (request.getDate() == null) {
            request.setDate(LocalDate.now());
        }

        // Handle file upload
        if (documentFile != null && !documentFile.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                String fileName = UUID.randomUUID().toString() + "_" + documentFile.getOriginalFilename();
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

                // Create directory if not exists
                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }

                java.nio.file.Path filePath = uploadPath.resolve(fileName);

                // Save the file locally
                documentFile.transferTo(filePath.toFile());

                // Save the relative path to the database
                request.setDocument(uploadDir + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Request savedRequest = requestRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
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
        return ResponseEntity.ok("Delete data success");
    }
}
