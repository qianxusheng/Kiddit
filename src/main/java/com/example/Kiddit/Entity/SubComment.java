package com.example.Kiddit.Entity;  

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "SubComments")
@NoArgsConstructor
@AllArgsConstructor
public class SubComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCommentId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "fk_created_by_user_id", nullable = false)
    private User createdByUser;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    @ManyToOne
    @JoinColumn(name = "fk_comment_id", nullable = false)
    private Comment comment;
}

