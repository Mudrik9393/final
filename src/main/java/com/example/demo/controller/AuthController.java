package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Optional<Role> roleOptional = roleRepository.findByRoleName(request.getRoleName());
        if (roleOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Role not found: " + request.getRoleName());
        }

        Role role = roleOptional.get();

        User user = new User();
        user.setUserName(request.getUserName());
        user.setZanId(request.getZanId());
        user.setEmail(request.getEmail());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setRole(role);

        userRepository.save(user);

        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOptional.get();
        boolean passwordMatches = BCrypt.checkpw(loginRequest.getPassword(), user.getPassword());

        if (!passwordMatches) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        // ✅ Jibu la JSON lenye userId na userName
        return ResponseEntity.ok(new LoginResponse(
                user.getUserId(),
                user.getUserName(),
                "Login successful"
        ));
    }

    // DTO for login request
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // DTO for register
    public static class RegisterRequest {
        private String userName;
        private String zanId;
        private String email;
        private String password;
        private String roleName;

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public String getZanId() { return zanId; }
        public void setZanId(String zanId) { this.zanId = zanId; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }
    }

    // ✅ DTO for login response
    public static class LoginResponse {
        private Long userId;
        private String userName;
        private String message;

        public LoginResponse(Long userId, String userName, String message) {
            this.userId = userId;
            this.userName = userName;
            this.message = message;
        }

        public Long getUserId() { return userId; }
        public String getUserName() { return userName; }
        public String getMessage() { return message; }
    }
}
