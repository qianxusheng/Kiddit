package com.example.Kiddit.DataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private int userId;
    private String bio;
    private String avatarUrl;
}