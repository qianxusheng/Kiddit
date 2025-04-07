package com.example.Kiddit.DataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String firstName;
    private String lastName;
    private Long userId;
    private String token;
}
