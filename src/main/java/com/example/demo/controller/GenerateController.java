package com.example.demo.controller;

import com.example.demo.model.Generate;
import com.example.demo.model.User;
import com.example.demo.Repository.GenerateRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/generate")
@CrossOrigin
public class GenerateController {

    @Autowired
    private GenerateRepository generateRepository;

    @Autowired
    private UserRepository userRepository;

    // Generate new control number for a user
    @PostMapping("/{userId}")
    public ResponseEntity<?> generateControlNumber(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = optionalUser.get();

        // Check if user already has a control number (optional logic)
        Optional<Generate> existing = generateRepository.findTopByUser(user);
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("User already has a control number");
        }

        String controlNumber = generateFormattedNumber();

        Generate generate = new Generate();
        generate.setControlNumber(controlNumber);
        generate.setUser(user);

        Generate saved = generateRepository.save(generate);

        return ResponseEntity.ok(saved);
    }

    // Get all control numbers for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getControlNumbersByUser(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = optionalUser.get();
        List<Generate> controlNumbers = generateRepository.findByUser(user);

        return ResponseEntity.ok(controlNumbers);
    }

    // Private method to generate fixed-format control number
    private String generateFormattedNumber() {
        String prefix = "200720"; // Fixed 6 digits
        Random rand = new Random();
        int suffix = rand.nextInt(900) + 100; // Random 3 digits from 100 to 999
        return prefix + suffix;
    }
}
