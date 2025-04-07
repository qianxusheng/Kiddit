package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.UserInfoResponseDTO;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private JwtUtil jwtUtil = new JwtUtil();  // Use the JwtUtil class

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public Map<String, Object> loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Generate JWT token upon successful login
        String token = jwtUtil.generateToken((long) user.getUserId(), user.getFirstName(), user.getLastName());

        // Prepare the response with both the token and user data
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);  // Return JWT token
        response.put("user", user);    // Return the user object

        return response;
        }     
    
    public UserInfoResponseDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId.intValue()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserInfoResponseDTO userInfo = new UserInfoResponseDTO();
        userInfo.setUserId((long) user.getUserId());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setEmail(user.getEmail());
        return userInfo;
    }

}
