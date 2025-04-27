package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.RegisterRequestDTO;
import com.example.Kiddit.DataTransferObject.LoginResponseDTO;
import com.example.Kiddit.DataTransferObject.UserInfoResponseDTO;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.UserProfile;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder(); // You could also inject this if preferred
    }

    /**
     * Registers a new user by saving the user details into the database.
     *
     * @param dto the registration details of the user
     * @throws IllegalArgumentException if the email is already registered
     */
    @Transactional
    public void registerUser(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setNickName(dto.getNickName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        UserProfile newProfile = new UserProfile();
        newProfile.setUser(user);  

        userProfileRepository.save(newProfile);
    }

    /**
     * Authenticates a user by checking the email and password.
     *
     * @param email    the email of the user trying to log in
     * @param password the password provided by the user
     * @return a response containing user details and JWT token
     * @throws IllegalArgumentException if the credentials are invalid
     */
    public LoginResponseDTO loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken((long) user.getUserId(), user.getFirstName(), user.getLastName());

        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setUserId((long) user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setToken(token);

        return dto;
    }

    /**
     * Fetches user information based on the user ID.
     *
     * @param userId the ID of the user whose information is to be retrieved
     * @return a response containing user information
     * @throws IllegalArgumentException if the user is not found
     */
    public UserInfoResponseDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserInfoResponseDTO userInfo = new UserInfoResponseDTO();
        userInfo.setUserId((long) user.getUserId());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setEmail(user.getEmail());

        return userInfo;
    }
}
