package com.example.Kiddit.Repository;

import com.example.Kiddit.Entity.Post;
import com.example.Kiddit.Entity.SubKiddit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findBySubkiddit(SubKiddit subkiddit, Pageable pageable);
}
