package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.UserProfileDTO;
import com.example.Kiddit.Service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable int userId) {
        UserProfileDTO userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable int userId, @RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO updatedProfile = userProfileService.updateUserProfile(userId, userProfileDTO);
        return ResponseEntity.ok(updatedProfile);
    }
}
