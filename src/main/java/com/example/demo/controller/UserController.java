package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;  // ← for encoding/checking passwords
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    // Inject RoleRepository so we can look up a Role by name
    @Autowired
    private RoleRepository roleRepository;

    // Inject PasswordEncoder (e.g. a BCryptPasswordEncoder bean)
    @Autowired
    private PasswordEncoder passwordEncoder;


    // ─────────────────────────────────────────────────────────────────────────────
    // ← EXISTING CRUD METHODS (unchanged)                                      →
    // ─────────────────────────────────────────────────────────────────────────────

    @GetMapping("/get")
    public List<User> getAllUser() {
        return userRepository.findAll();
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
                // Re‐encode the password if it has changed
                if (!updatedUser.getPassword().equals(user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                User savedUser = userRepository.save(user);
                return ResponseEntity.ok(savedUser);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // ─────────────────────────────────────────────────────────────────────────────
    // ← NEW: SIGNUP / REGISTER (only for customers)                            →
    // ─────────────────────────────────────────────────────────────────────────────

    /**
     * Signup (register) a new User. The request must include:
     *   {
     *     "userName": "...",
     *     "zanId": "...",
     *     "email": "...",
     *     "password": "...",
     *     "roleName": "customer"   // or any other valid roleName
     *   }
     *
     * Only roles that exist and are active (status=true) can be assigned.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> payload) {
        String userName = payload.get("userName");
        String zanId    = payload.get("zanId");
        String email    = payload.get("email");
        String rawPass  = payload.get("password");
        String roleName = payload.get("roleName");  // e.g. "customer", "customer_care", "meter_reader"

        if (userName == null || zanId == null || email == null || rawPass == null || roleName == null) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Missing one of: userName, zanId, email, password, roleName"));
        }

        // 1) Check if email is already registered
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Email already registered"));
        }

        // 2) Lookup Role by name and ensure it's active
        Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);
        if (roleOpt.isEmpty() || !Boolean.TRUE.equals(roleOpt.get().getStatus())) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Role not found or not active: " + roleName));
        }
        Role role = roleOpt.get();

        // 3) Create and save the new User
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


    // ─────────────────────────────────────────────────────────────────────────────
    // ← NEW: LOGIN (all roles: customer, customer_care, meter_reader)           →
    // ─────────────────────────────────────────────────────────────────────────────

    /**
     * Login expects JSON with:
     *   {
     *     "email": "...",
     *     "password": "...",
     *     "roleName": "customer"    // must match the user's assigned roleName
     *   }
     *
     * Responds with 200 and user details if successful, or 401/400 on error.
     */
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

        // 1) Lookup user by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }
        User user = userOpt.get();

        // 2) Verify that the requested roleName matches the user's assigned role
        if (!user.getRole().getRoleName().equalsIgnoreCase(roleName)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Role mismatch"));
        }

        // 3) Check password
        if (!passwordEncoder.matches(rawPass, user.getPassword())) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }

        // 4) Successful login → return minimal user info (omit password)
        return ResponseEntity.ok(Map.of(
            "message",  "Login successful",
            "userId",   user.getUserId(),
            "userName", user.getUserName(),
            "zanId",    user.getZanId(),
            "email",    user.getEmail(),
            "roleName", user.getRole().getRoleName()
        ));
    }
}
