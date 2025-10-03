package com.example.Qolzy.repository;

import com.example.Qolzy.model.follow.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);
    List<Follow> findByFollowerId(Long followerId);
}
