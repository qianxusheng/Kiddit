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

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileDTO getUserProfile(int userId) {
        UserProfile profile = userProfileRepository.findByUser_UserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("User profile not found.");
        }

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(userId);
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());
        return dto;
    }

    public UserProfileDTO updateUserProfile(int userId, UserProfileDTO userProfileDTO) {
        UserProfile profile = userProfileRepository.findByUser_UserId(userId);
        if (profile == null) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new IllegalArgumentException("User not found.");
            }
            profile = new UserProfile();
            profile.setUser(userOptional.get());
        }

        profile.setBio(userProfileDTO.getBio());
        profile.setAvatarUrl(userProfileDTO.getAvatarUrl());
        userProfileRepository.save(profile);
        userProfileDTO.setUserId(profile.getUser().getUserId());
        
        return userProfileDTO;
    }
}
