package com.example.Kiddit.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Posts", indexes = {
    @Index(name = "idx_subkiddit_id", columnList = "fk_subkiddit_id"),
    // @Index(name = "idx_created_at", columnList = "created_at"),  -- Not used at this moment
    // @Index(name = "idx_created_by_user_id", columnList = "fk_created_by_user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "fk_subkiddit_id", nullable = false)
    private SubKiddit subkiddit;

    @ManyToOne
    @JoinColumn(name = "fk_created_by_user_id", nullable = false)
    private User createdByUser;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
