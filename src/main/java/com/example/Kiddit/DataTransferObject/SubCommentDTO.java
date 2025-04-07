package com.example.Kiddit.DataTransferObject;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
public class SubCommentDTO {
    private Long subCommentId;
    private String content;
    private String createdByFirstName;
    private String createdByLastName;
    private LocalDateTime createdAt;
}