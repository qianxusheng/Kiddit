package com.example.Kiddit.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Kiddit.Entity.SubkidditCategoryUserView;

@Repository
public interface SubkidditCategoryUserViewRepository extends JpaRepository<SubkidditCategoryUserView, Long> {
    Page<SubkidditCategoryUserView> findByCategoryId(Long categoryId, Pageable pageable);
}