package com.example.Kiddit.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recent_comments_view")  
public class RecentCommentView {

    @Id
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content")
    private String content;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_subject")
    private String postSubject;

    @Column(name = "post_created_at")
    private LocalDateTime postCreatedAt;

}