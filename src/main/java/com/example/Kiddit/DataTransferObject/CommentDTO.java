package com.example.Kiddit.DataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDTO {

    private Long commentId;
    private String content;
    private String createdByFirstName;
    private String createdByLastName;
    private LocalDateTime createdAt;
}
