package com.example.Kiddit.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "CommentVotes", uniqueConstraints = {
@UniqueConstraint(columnNames = {"user_id", "comment_id"})
    },
    indexes = {
        @Index(name = "idx_comment_id", columnList = "comment_id"),
        @Index(name = "idx_user_id", columnList = "user_id")}
        )

public class CommentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;

    private LocalDateTime votedAt;
}

