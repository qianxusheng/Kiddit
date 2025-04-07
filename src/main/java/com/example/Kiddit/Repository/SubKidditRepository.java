package com.example.Kiddit.Repository;

import com.example.Kiddit.Entity.SubKiddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SubKidditRepository extends JpaRepository<SubKiddit, Long> {
    Page<SubKiddit> findByCategory_CategoryId(long categoryId, Pageable pageable);
}
