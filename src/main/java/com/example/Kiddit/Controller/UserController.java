package com.example.Kiddit.Controller;

import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

// entity(table) -> repository(ez query) -> service(complex logic) -> controller(http request)
@CrossOrigin(origins = "http://localhost:4200")  // CORS configuration for frontend access
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired // Constructor-based dependency injection
    public UserController(UserService userService) { // java contructor
        this.userService = userService;
    }

    // Registration endpoint
    // @RequestBody json format (many key-value pairs)
    // also return json format message to the frontend
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        
        try {
            userService.registerUser(user);  
            response.put("message", "User registered successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.CREATED); // HttpStatus.CREATED = 201, it's http status code
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("status", "error");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // HttpStatus.BAD_REQUEST = 400
        }
    }

    // Login endpoint
    // Map<String, String> credentials = {email: "email", password: "password"}, same as json format
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Map<String, String> response = new HashMap<>();

        try {
            User user = userService.loginUser(email, password);  
            response.put("message", "Login successful: " + user.getFirstName());
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("status", "error");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
