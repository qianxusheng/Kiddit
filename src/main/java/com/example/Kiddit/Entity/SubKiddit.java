package com.example.Kiddit.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Subkiddits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubKiddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subkiddit_id")
    private Long subkidditId;

    @ManyToOne
    @JoinColumn(name = "fk_category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "fk_created_by_user_id", nullable = false)
    private User createdByUser;

    @Column(name = "subject", length = 50)
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
