package com.example.Kiddit.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.Kiddit.Repository.AvatarRepository;
import com.example.Kiddit.Repository.UserProfileRepository;
import com.example.Kiddit.Entity.Avatar;
import com.example.Kiddit.Entity.UserProfile;


@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final UserProfileRepository userProfileRepository;

    public AvatarService(AvatarRepository avatarRepository, UserProfileRepository userProfileRepository) {
        this.avatarRepository = avatarRepository;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Upload or update avatar for a specific user profile.
     *
     * @param profileId ID of the user profile
     * @param imageData image bytes
     */
    @Transactional
    public void uploadAvatar(Long profileId, byte[] imageData) {
        UserProfile profile = userProfileRepository.findById(profileId)
            .orElseThrow(() -> new IllegalArgumentException("UserProfile not found"));

        // If there's an old avatar, delete it first
        Avatar oldAvatar = profile.getAvatar();
        if (oldAvatar != null) {
            profile.setAvatar(null); // break the link first
            userProfileRepository.save(profile); // ensure DB sync
            avatarRepository.deleteById(oldAvatar.getId());
        }

        // Save new avatar
        Avatar newAvatar = new Avatar();
        newAvatar.setAvatar(imageData);
        avatarRepository.save(newAvatar);

        // Link new avatar to profile
        profile.setAvatar(newAvatar);
        userProfileRepository.save(profile);
    }

    /**
     * Get avatar binary data by user profile ID.
     *
     * @param userProfileId ID of the user profile
     * @return byte array of avatar if exists
     */
    public byte[] getAvatarByUserProfileId(Long userProfileId) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        Avatar avatar = profile.getAvatar();
        if (avatar == null) {
            return null;
        }

        return avatar.getAvatar();
    }
    
}
