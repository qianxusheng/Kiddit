package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.UserProfileDTO;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.UserProfile;
import com.example.Kiddit.Repository.UserProfileRepository;
import com.example.Kiddit.Repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    // Constructor to inject dependencies
    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
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
        dto.setAvatarUrl(profile.getAvatarUrl());
        return dto;
    }

    /**
     * Updates a user's profile information.
     *
     * @param userId the ID of the user whose profile is to be updated
     * @param userProfileDTO the new profile data to be updated
     * @return a UserProfileDTO with the updated profile information
     * @throws IllegalArgumentException if the user or profile is not found
     */
    public UserProfileDTO updateUserProfile(int userId, UserProfileDTO userProfileDTO) {
        // Find the existing user profile
        UserProfile profile = userProfileRepository.findByUser_UserId(userId);

        // If no profile is found, check if the user exists and create a new profile if necessary
        if (profile == null) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("User not found for user ID: " + userId);
            }
            profile = new UserProfile();
            profile.setUser(userOptional.get());
        }

        // Update the profile fields
        profile.setBio(userProfileDTO.getBio());
        profile.setAvatarUrl(userProfileDTO.getAvatarUrl());

        // Save the updated profile back to the database
        userProfileRepository.save(profile);

        // Set the user ID in the DTO before returning it
        userProfileDTO.setUserId(profile.getUser().getUserId());
        
        return userProfileDTO;
    }
}
