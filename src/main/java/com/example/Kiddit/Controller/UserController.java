package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.LoginResponseDTO;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Kiddit.DataTransferObject.RegisterRequestDTO;
import com.example.Kiddit.DataTransferObject.UserInfoResponseDTO;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired // Constructor-based dependency injection
    public UserController(UserService userService) { // java contructor
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            User user = new User();
            user.setNickName(registerRequestDTO.getNickName());
            user.setFirstName(registerRequestDTO.getFirstName());
            user.setLastName(registerRequestDTO.getLastName());
            user.setEmail(registerRequestDTO.getEmail());
            user.setPassword(registerRequestDTO.getPassword());
    
            userService.registerUser(user);
    
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            Map<String, Object> loginResult = userService.loginUser(email, password);
            String token = (String) loginResult.get("token");
            User user = (User) loginResult.get("user");

            // Create a new DTO to return the user data
            LoginResponseDTO loginResponse = new LoginResponseDTO();
            loginResponse.setFirstName(user.getFirstName());
            loginResponse.setLastName(user.getLastName());
            loginResponse.setUserId((long) user.getUserId());
            // Assuming the token is generated on the server-side after login
            loginResponse.setToken(token);

            return ResponseEntity.ok(loginResponse); // Return OK with the LoginResponseDTO
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 Unauthorized if credentials are wrong
        }
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo(@RequestParam("userId") Long userId) {
        try {
            UserInfoResponseDTO userInfo = userService.getUserInfo(userId);
            return ResponseEntity.ok(userInfo); 
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
