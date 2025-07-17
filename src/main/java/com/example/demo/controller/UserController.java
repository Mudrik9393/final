package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/get")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return userRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}


    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>("Delete data success", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUserName(updatedUser.getUserName());
                user.setZanId(updatedUser.getZanId());
                user.setEmail(updatedUser.getEmail());
                if (!updatedUser.getPassword().equals(user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                User savedUser = userRepository.save(user);
                return ResponseEntity.ok(savedUser);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> payload) {
        String userName = payload.get("userName");
        String zanId    = payload.get("zanId");
        String email    = payload.get("email");
        String rawPass  = payload.get("password");
        String roleName = payload.get("roleName");

        if (userName == null || zanId == null || email == null || rawPass == null || roleName == null) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Missing one of: userName, zanId, email, password, roleName"));
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Email already registered"));
        }

        Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);
        if (roleOpt.isEmpty() || !Boolean.TRUE.equals(roleOpt.get().getStatus())) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Role not found or not active: " + roleName));
        }
        Role role = roleOpt.get();

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setZanId(zanId);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(rawPass));
        newUser.setRole(role);

        User saved = userRepository.save(newUser);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Map.of(
                "message", "User registered successfully",
                "userId", saved.getUserId()
            ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email    = payload.get("email");
        String rawPass  = payload.get("password");
        String roleName = payload.get("roleName");

        if (email == null || rawPass == null || roleName == null) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Missing one of: email, password, roleName"));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }
        User user = userOpt.get();

        if (!user.getRole().getRoleName().equalsIgnoreCase(roleName)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Role mismatch"));
        }

        if (!passwordEncoder.matches(rawPass, user.getPassword())) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.ok(Map.of(
            "message",  "Login successful",
            "userId",   user.getUserId(),
            "userName", user.getUserName(),
            "zanId",    user.getZanId(),
            "email",    user.getEmail(),
            "roleName", user.getRole().getRoleName()
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // ← NEW: Get all customers (for dropdown selection by meter reader)
    // ─────────────────────────────────────────────────────────────────────────────

    @GetMapping("/customers")
    public List<CustomerDto> getCustomers() {
        return userRepository.findAll()
            .stream()
            .filter(user -> user.getRole() != null &&
                            "CUSTOMER".equalsIgnoreCase(user.getRole().getRoleName()))
            .map(user -> new CustomerDto(user.getUserId(), user.getUserName()))
            .toList();
    }

    public static class CustomerDto {
        private Long userId;
        private String userName;

        public CustomerDto(Long userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }
    }
}
