package com.example.demo.Services;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Create a new user and assign a specific role
    public User createUserWithRole(User user, String roleName) {
        // 1) Check for duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        // 2) Look up the Role entity and ensure it's active
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Role not found: " + roleName));

        if (role.getStatus() == null || !role.getStatus()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Role is not active: " + roleName);
        }

        // 3) Hash the password and set role
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        user.setRole(role);

        return userRepository.save(user);
    }

    // Delete user by ID
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    // Update user (does not change role)
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUserName(updatedUser.getUserName());
                user.setZanId(updatedUser.getZanId());
                user.setEmail(updatedUser.getEmail());

                // Only update password if a new one is provided
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    String hashed = BCrypt.hashpw(updatedUser.getPassword(), BCrypt.gensalt());
                    user.setPassword(hashed);
                }
                return userRepository.save(user);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    // Register a new Customer (uses roleName = "customer")
    public User registerCustomer(String userName, String zanId, String email, String password) {
        // 1) Ensure email is not already in use
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        // 2) Fetch the "customer" role and verify it is active
        Role customerRole = roleRepository.findByRoleName("customer")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Customer role not found"));

        if (customerRole.getStatus() == null || !customerRole.getStatus()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Customer role is not active");
        }

        // 3) Create new User, hash password, assign role
        User user = new User();
        user.setUserName(userName);
        user.setZanId(zanId);
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole(customerRole);

        return userRepository.save(user);
    }

    // Authenticate user by email, password, and roleName
    public User authenticateUser(String email, String password, String roleName) {
        // 1) Lookup user by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        User user = userOpt.get();

        // 2) Check role matches
        if (user.getRole() == null ||
            !user.getRole().getRoleName().equalsIgnoreCase(roleName)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Role mismatch");
        }

        // 3) Verify password using BCrypt
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return user;
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users with a specific role
    public List<User> getUsersByRole(String roleName) {
        return userRepository.findByRole_RoleName(roleName);
    }
}
