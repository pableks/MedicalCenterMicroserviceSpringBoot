package com.example.medicalmicroservice.service;


import java.util.List;

import com.example.medicalmicroservice.model.User;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
    User authenticateUser(String username, String password);
    User getUserById(Long id);
    User updateUser(User user); 
    List<User> getAllUsers();
    
}