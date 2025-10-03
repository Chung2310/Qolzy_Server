package com.example.Qolzy.repository;

import com.example.Qolzy.model.comment.Comment;
import com.example.Qolzy.model.comment.CommentResponse;
import com.example.Qolzy.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostIdAndParentIsNullAndIsDeletedFalse(Long postId, Pageable pageable);
    Page<Comment> findByParentIdAndIsDeletedFalse(Long commentId, Pageable pageable);
    boolean existsByParentId(Long commentId);
    int countByParentId(Long commentId);
}
