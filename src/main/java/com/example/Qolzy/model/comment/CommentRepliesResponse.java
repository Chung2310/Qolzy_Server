package com.example.Qolzy.model.comment;

import com.example.Qolzy.dto.UserEntityDTO;

import java.time.LocalDateTime;
import java.util.List;

public class CommentRepliesResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserEntityDTO userComment;
    private int likes;
    private boolean isReplies;
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

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isReplies() {
        return isReplies;
    }

    public void setReplies(boolean replies) {
        isReplies = replies;
    }
}
