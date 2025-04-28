package com.example.Kiddit.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Kiddit.Entity.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}