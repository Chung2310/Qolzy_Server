package com.example.Qolzy.model.comment;

import com.example.Qolzy.dto.UserEntityDTO;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserEntityDTO userComment;
    private Long parenId;
    private int likes;
    private int level;
    private int countComment;
    private boolean likedByCurrentUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserEntityDTO getUserComment() {
        return userComment;
    }

    public void setUserComment(UserEntityDTO userComment) {
        this.userComment = userComment;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCountComment() {
        return countComment;
    }

    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public Long getParenId() {
        return parenId;
    }

    public void setParenId(Long parenId) {
        this.parenId = parenId;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }
}
