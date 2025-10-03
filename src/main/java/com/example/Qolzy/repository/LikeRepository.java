package com.example.Qolzy.repository;

import com.example.Qolzy.model.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void  deleteByPostIdAndUserId(Long postId, Long userId);
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    void  deleteByCommentIdAndUserId(Long commentId, Long userId);
}
