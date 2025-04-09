package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.LoginResponseDTO;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user with the provided registration details.
     *
     * @param registerRequestDTO the registration details including user information
     * @return ResponseEntity with a CREATED status if registration is successful, or BAD_REQUEST if there's an issue
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            userService.registerUser(registerRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Logs in a user using the provided credentials (email and password).
     *
     * @param credentials a map containing the user's email and password
     * @return ResponseEntity with a LoginResponseDTO on successful login, or UNAUTHORIZED status if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            LoginResponseDTO response = userService.loginUser(email, password);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Retrieves detailed information of a user based on their user ID.
     *
     * @param userId the ID of the user whose information is to be fetched
     * @return ResponseEntity with the UserInfoResponseDTO containing the user's details, or INTERNAL_SERVER_ERROR if an error occurs
     */
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
