package com.example.medicalmicroservice.controller;

import com.example.medicalmicroservice.model.User;
import com.example.medicalmicroservice.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User is already logged in. Please log out to register a new user.");
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User is already logged in. Please log out first.");
        }

        User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            session.setAttribute("user", authenticatedUser);
            return ResponseEntity.ok("User logged in successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("User logged out successfully");
    }

    @GetMapping("/current-user")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            String roleName = currentUser.getRole();
            CurrentUserResponse response = new CurrentUserResponse(currentUser, roleName);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/update-role/{userId}")
    public ResponseEntity<String> updateUserRole(@PathVariable Long userId, @RequestBody String newRole,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getRole().equals("admin")) {
            User userToUpdate = userService.getUserById(userId);
            if (userToUpdate != null) {
                userToUpdate.setRole(newRole);
                userService.updateUser(userToUpdate);
                return ResponseEntity.ok("User role updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Access denied. Only admins can update user roles.");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getRole().equals("admin")) {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Custom response class
    static class CurrentUserResponse {
        private User user;
        private String roleName;

        public CurrentUserResponse(User user, String roleName) {
            this.user = user;
            this.roleName = roleName;
        }

        public User getUser() {
            return user;
        }

        public String getRoleName() {
            return roleName;
        }
    }
}