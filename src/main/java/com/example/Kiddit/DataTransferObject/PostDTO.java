package com.example.Kiddit.DataTransferObject;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDTO {
    private Long postId;
    private String subject;
    private String description;
    private String createdByFirstName;
    private String createdByLastName;
    private LocalDateTime createdAt;
}