package com.example.Kiddit.Repository;

import com.example.Kiddit.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.example.Kiddit.Entity.User; 

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByUsersContaining(User user, Pageable pageable);
}
