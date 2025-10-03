package com.example.Qolzy.repository;

import com.example.Qolzy.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByIsDeletedFalse(Pageable pageable);
    Page<Post> findByIsDeletedFalseAndUserId(Long userId, Pageable pageable);
}
