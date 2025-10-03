package com.example.Qolzy.model.follow;

import com.example.Qolzy.dto.UserEntityDTO;

import java.time.LocalDateTime;

public class FollowResponse {
    private Long id;
    private UserEntityDTO following;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntityDTO getFollowing() {
        return following;
    }

    public void setFollowing(UserEntityDTO following) {
        this.following = following;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
