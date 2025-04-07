package com.example.Kiddit.DataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponseDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
}