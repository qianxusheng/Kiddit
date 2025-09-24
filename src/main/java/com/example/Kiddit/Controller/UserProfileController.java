package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.UserProfileDTO;
import com.example.Kiddit.Entity.RecentCommentView;
import com.example.Kiddit.Service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
     * Updates the user's bio.
     *
     * @param userId the ID of the user whose bio is to be updated
     * @param bio the new bio data
     * @return ResponseEntity indicating the success of the update
     */
    @PutMapping("/{userId}/bio")
    public ResponseEntity<Void> updateBio(@PathVariable int userId, @RequestBody String bio) {
        userProfileService.updateBio(userId, bio);
        return ResponseEntity.ok().build();  // No content to return
    }

    @GetMapping("/{userId}/recent-comments")
    public List<RecentCommentView> getRecentComments(@PathVariable Long userId) {
        return userProfileService.getRecentComments(userId);
    }
}
