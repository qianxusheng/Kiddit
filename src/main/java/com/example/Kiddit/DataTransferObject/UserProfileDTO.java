package com.example.Kiddit.DataTransferObject;

import com.example.Kiddit.Entity.Avatar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private int profileId;
    private int userId;
    private String bio;
    private Avatar avatar;
}