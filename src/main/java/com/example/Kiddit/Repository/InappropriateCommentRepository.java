package com.example.Kiddit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Kiddit.Entity.InappropriateComment;

@Repository
public interface InappropriateCommentRepository extends JpaRepository<InappropriateComment, Long> {
}
