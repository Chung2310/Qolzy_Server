package com.example.Qolzy.repository;

import com.example.Qolzy.model.reel.Reel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReelRepository extends JpaRepository<Reel, Long> {
    Page<Reel> findByIsDeletedFalse(Pageable pageable);
    Page<Reel> findByIsDeletedFalseAndUserId(Long userId, Pageable pageable);
}
