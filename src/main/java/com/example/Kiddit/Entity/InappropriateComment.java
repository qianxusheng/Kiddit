package com.example.Kiddit.Entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inappropriate_comments")
@Data
public class InappropriateComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String label = "inappropriate";

    @Column(nullable = false)
    private Boolean reviewed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
