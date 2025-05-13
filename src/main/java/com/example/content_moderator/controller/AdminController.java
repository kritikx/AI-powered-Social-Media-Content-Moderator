package com.example.content_moderator.controller;

import com.example.content_moderator.model.UserStrike;
import com.example.content_moderator.repository.UserStrikeRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserStrikeRepository userStrikeRepository;

    // Get all users with strikes
    @GetMapping("/users")
    public List<UserStrike> getAllUsers() {
        return userStrikeRepository.findAll();
    }

    // Get single user by ID
    @GetMapping("/user/{userId}")
    public Optional<UserStrike> getUserById(@PathVariable String userId) {
        return userStrikeRepository.findById(userId);
    }

    // Reset a user's strikes
    @PostMapping("/user/{userId}/reset")
    public String resetUserStrikes(@PathVariable String userId) {
        Optional<UserStrike> userStrikeOpt = userStrikeRepository.findById(userId);
        if (userStrikeOpt.isPresent()) {
            UserStrike userStrike = userStrikeOpt.get();
            userStrike.setStrikeCount(0);
            userStrikeRepository.save(userStrike);
            return "Strikes reset for user: " + userId;
        } else {
            return "User not found.";
        }
    }

    // Delete a user (optional, admin danger zone!)
    @DeleteMapping("/user/{userId}")
    public String deleteUser(@PathVariable String userId) {
        if (userStrikeRepository.existsById(userId)) {
            userStrikeRepository.deleteById(userId);
            return "User deleted: " + userId;
        } else {
            return "User not found.";
        }
    }

    // Ban a user
    @PostMapping("/ban/{userId}")
    public ResponseEntity<?> banUser(@PathVariable String userId) {
        try {
            Optional<UserStrike> optionalUser = userStrikeRepository.findById(userId);
            if (optionalUser.isPresent()) {
                UserStrike userStrike = optionalUser.get();
                userStrike.setBanned(true);
                userStrikeRepository.save(userStrike);

                // Return consistent JSON response
                Map<String, String> response = new HashMap<>();
                response.put("message", "User banned successfully");
                return ResponseEntity.ok(response);
            } else {
                // Return JSON error response
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error processing ban request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Unban a user
    @PostMapping("/unban/{userId}")
    public ResponseEntity<?> unbanUser(@PathVariable String userId) {
        Optional<UserStrike> optionalUser = userStrikeRepository.findById(userId); // changed to UserStrike
        if (optionalUser.isPresent()) {
            UserStrike userStrike = optionalUser.get();
            userStrike.setBanned(false);
            userStrikeRepository.save(userStrike);
            return ResponseEntity.ok("User unbanned successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Fetch all banned users
    @GetMapping("/banned-users")
    public ResponseEntity<List<UserStrike>> getBannedUsers() {
        List<UserStrike> bannedUsers = userStrikeRepository.findByBanned(true);
        if (bannedUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(bannedUsers);
        }
    }

    // Temporary hardcoded admin username/password
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password123";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
