package com.example.Kiddit.DataTransferObject;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import com.example.Kiddit.Entity.VoteType;

@Setter
@Getter
public class PostDTO {
    private Long postId;
    private String subject;
    private String description;
    private String createdByFirstName;
    private String createdByLastName;
    private LocalDateTime createdAt;
    private Long userId;
    private int upvotes;
    private int downvotes;
    private VoteType userVoteStatus;
}