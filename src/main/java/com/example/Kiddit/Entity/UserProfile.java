package com.example.Kiddit.Entity;

// import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "UserProfiles")
// @RedisHash("UserProfiles")   // redis small use test
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private int profileId;

    @OneToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;
}
