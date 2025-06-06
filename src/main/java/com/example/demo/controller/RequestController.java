package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.RequestRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Request;
import com.example.demo.model.User;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/create")
    public ResponseEntity<Request> createRequest(@RequestBody Request request) {
        // Generate account number automatically
        request.setAccountNumber("ACC" + UUID.randomUUID().toString().substring(0, 8));
        request.setMessage(null);
        if (request.getDate() == null) {
            request.setDate(LocalDate.now());
        }

        // Save request directly (no User involved)
        Request savedRequest = requestRepository.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
    }

    @GetMapping("/get")
    public List<Request> getAllRequest() {
        return requestRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
        requestRepository.deleteById(id);
        return ResponseEntity.ok("Delete data success");
    }
}
