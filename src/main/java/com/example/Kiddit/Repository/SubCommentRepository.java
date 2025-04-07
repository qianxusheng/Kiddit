package com.example.Kiddit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.Kiddit.Entity.SubComment;

@Repository
public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    Page<SubComment> findByComment_CommentId(Long commentId, Pageable pageable);
}