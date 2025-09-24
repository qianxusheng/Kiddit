package com.example.Kiddit.DataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String nickName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
