package com.example.Kiddit.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import com.example.Kiddit.Service.AvatarService; 
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/avatars")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    /**
     * Get avatar image by UserProfile ID.
     *
     * @param userProfileId the user's profile ID
     * @return avatar image bytes
     */
    @GetMapping("/user-profile/{userProfileId}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long userProfileId) {
        byte[] image = avatarService.getAvatarByUserProfileId(userProfileId);
        if (image == null) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // You may detect content type dynamically
                .body(image);
    }

    /**
     * Uploads a new avatar and associates it with the given user profile.
     *
     * @param profileId the ID of the UserProfile
     * @param file the uploaded avatar file
     * @return ResponseEntity with success message
     */
    @PostMapping("/upload/{profileId}")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file) {
        try {
            byte[] imageData = file.getBytes();
            avatarService.uploadAvatar(profileId, imageData);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Avatar uploaded successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Upload failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
