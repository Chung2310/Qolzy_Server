package com.example.Qolzy.model.story;

import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.post.PostMedia;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stories")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostMedia> medias;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.expiresAt = createdAt.plusDays(1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<PostMedia> getMedias() {
        return medias;
    }

    public void setMedias(List<PostMedia> media) {
        this.medias = media;
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
