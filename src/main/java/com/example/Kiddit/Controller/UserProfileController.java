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

    /**
     * Retrieves the user profile based on the provided user ID.
     *
     * @param userId the ID of the user whose profile is to be fetched
     * @return ResponseEntity containing the UserProfileDTO if successful
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable int userId) {
        UserProfileDTO userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    /**
     * Updates the user profile with the provided user profile data.
     *
     * @param userId the ID of the user whose profile is to be updated
     * @param userProfileDTO the updated user profile data
     * @return ResponseEntity containing the updated UserProfileDTO
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable int userId, @RequestBody UserProfileDTO userProfileDTO) {
        UserProfileDTO updatedProfile = userProfileService.updateUserProfile(userId, userProfileDTO);
        return ResponseEntity.ok(updatedProfile);
    }
}
