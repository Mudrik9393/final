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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Repository.UserRepository;
import com.example.demo.model.User;
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/get")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<User> createComplaint(@RequestBody User user) {
        User savedUser = userRepository.save(user); 
        return ResponseEntity.ok(savedUser);
    }

    
     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
        return new ResponseEntity<>("Delete data success", HttpStatus.OK);
    }
}
