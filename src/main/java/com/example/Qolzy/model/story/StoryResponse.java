package com.example.Qolzy.model.story;

import com.example.Qolzy.dto.PostMediaDTO;
import com.example.Qolzy.dto.UserEntityDTO;

import java.time.LocalDateTime;
import java.util.List;

public class StoryResponse {
    private Long id;
    private UserEntityDTO user;
    private List<PostMediaDTO> medias;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntityDTO getUser() {
        return user;
    }

    public void setUser(UserEntityDTO user) {
        this.user = user;
    }

    public List<PostMediaDTO> getMedias() {
        return medias;
    }

    public void setMedias(List<PostMediaDTO> medias) {
        this.medias = medias;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
