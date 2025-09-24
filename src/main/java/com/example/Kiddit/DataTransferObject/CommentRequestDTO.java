package com.example.Kiddit.DataTransferObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    private Long userId;
    private String content;
}
