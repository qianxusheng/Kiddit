package com.example.Kiddit.DataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.example.Kiddit.Entity.VoteType;

@Getter
@Setter
public class CommentDTO {
    private Long commentId;
    private String content;
    private String createdByFirstName;
    private String createdByLastName;
    private LocalDateTime createdAt;
    private int upvotes;
    private int downvotes;
    private VoteType userVoteStatus;
}
