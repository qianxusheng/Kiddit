package com.example.Kiddit.Entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subkiddit_category_user_view")
public class SubkidditCategoryUserView {
    @Id
    @Column(name = "subkiddit_id")  
    private Long subkidditId;

    @Column(name = "fk_category_id")
    private Long fkCategoryId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "fk_created_by_user_id")
    private Long fkCreatedByUserId;

    @Column(name = "subkiddit_description")
    private String subkidditDescription;

    @Column(name = "subkiddit_subject")
    private String subkidditSubject;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_description")
    private String categoryDescription;

    @Column(name = "category_subject")
    private String categorySubject;

    @Column(name = "creator_user_id")
    private Long creatorUserId;

    @Column(name = "creator_email")
    private String creatorEmail;

    @Column(name = "creator_first_name")
    private String creatorFirstName;

    @Column(name = "creator_last_name")
    private String creatorLastName;

    @Column(name = "creator_nick_name")
    private String creatorNickName;
}
