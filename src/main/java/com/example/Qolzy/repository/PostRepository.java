package com.example.Qolzy.repository;

import com.example.Qolzy.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByIsDeletedFalse(Pageable pageable);
    Page<Post> findByIsDeletedFalseAndUserId(Long userId, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Post p JOIN p.medias m WHERE m.type = com.example.Qolzy.model.post.PostMedia.MediaType.VIDEO")
    Page<Post> findPostsWithVideos(Pageable pageable);

    Post findPostById(Long id);
}
