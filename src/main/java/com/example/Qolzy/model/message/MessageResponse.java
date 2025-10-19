package com.example.Qolzy.model.message;

import com.example.Qolzy.dto.UserEntityDTO;

import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private UserEntityDTO receiver;
    private UserEntityDTO sender;
    private String content;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntityDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntityDTO receiver) {
        this.receiver = receiver;
    }

    public UserEntityDTO getSender() {
        return sender;
    }

    public void setSender(UserEntityDTO sender) {
        this.sender = sender;
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
}
