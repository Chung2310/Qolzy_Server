package com.example.Qolzy.repository;

import com.example.Qolzy.model.story.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findStoriesByUserIdNotAndExpiresAtAfter(Long userId, LocalDateTime now, Pageable pageable);
    Page<Story> findByUserId(Long userId, Pageable pageable);
    List<Story> findByUserIdInAndExpiresAtAfter(List<Long> userIds, LocalDateTime now);
    Story findStoryById(Long id);
}
