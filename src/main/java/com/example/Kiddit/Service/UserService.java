package com.example.Kiddit.Service;

import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register new user
    public User registerUser(User user) {
        // Check if email is already taken
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        
        return userRepository.save(user);
    }

    // Login user
    public User loginUser(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = existingUser.get();
        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }
}
