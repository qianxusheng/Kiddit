package com.example.Kiddit.Repository;

import com.example.Kiddit.Entity.RecentCommentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface RecentCommentViewRepository extends JpaRepository<RecentCommentView, Long> {
    @Query("SELECT r FROM RecentCommentView r WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    List<RecentCommentView> findRecentCommentsByUserId(@Param("userId") Long userId, Pageable pageable);
}
