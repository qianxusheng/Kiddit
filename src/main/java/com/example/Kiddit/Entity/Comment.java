    package com.example.Kiddit.Entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "Comments")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Comment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long commentId;

        @ManyToOne
        @JoinColumn(name = "fk_post_id", nullable = false)
        private Post post;

        @ManyToOne
        @JoinColumn(name = "fk_created_by_user_id", nullable = false)
        private User createdByUser;

        @Column(columnDefinition = "TEXT")
        private String content;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @PrePersist
        public void prePersist() {
            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }
        }
    }
