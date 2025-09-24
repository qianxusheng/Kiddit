package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.UserProfileDTO;
import com.example.Kiddit.Entity.RecentCommentView;
import com.example.Kiddit.Entity.UserProfile;
import com.example.Kiddit.Repository.RecentCommentViewRepository;
import com.example.Kiddit.Repository.UserProfileRepository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final RecentCommentViewRepository recentCommentViewRepository;

    // Constructor to inject dependencies
    public UserProfileService(UserProfileRepository userProfileRepository, RecentCommentViewRepository recentCommentViewRepository) {
        this.recentCommentViewRepository = recentCommentViewRepository;
        this.userProfileRepository = userProfileRepository;

    }

    /**
     * Retrieves the profile details for a user by their user ID.
     *
     * @param userId the ID of the user whose profile is to be fetched
     * @return a UserProfileDTO containing the user's profile information
     * @throws IllegalArgumentException if the profile is not found
     */
    public UserProfileDTO getUserProfile(int userId) {
        UserProfile profile = userProfileRepository.findByUser_UserId(userId);
        
        // If no profile is found, throw an exception
        if (profile == null) {
            throw new IllegalArgumentException("User profile not found for user ID: " + userId);
        }

        // Convert UserProfile entity to UserProfileDTO
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(userId);
        dto.setBio(profile.getBio());
        dto.setAvatar(profile.getAvatar());
        dto.setProfileId(profile.getProfileId());
        return dto;
    }

    // update bio
    public void updateBio(int userId, String newBio) {
        UserProfile profile = userProfileRepository.findByUser_UserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("UserProfile not found for user ID: " + userId);
        }
    
        profile.setBio(newBio);
        userProfileRepository.save(profile);
    }

    public List<RecentCommentView> getRecentComments(Long userId) {
        Pageable pageable = PageRequest.of(0, 5); // Limit to 5 recent comments
        List<RecentCommentView> comments = recentCommentViewRepository.findRecentCommentsByUserId(userId, pageable);
        return comments;
    }
}
