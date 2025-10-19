package com.example.Qolzy.model.contact;

import com.example.Qolzy.dto.UserEntityDTO;

import java.time.LocalDateTime;

public class ContactResponse {
    private Long id;
    private UserEntityDTO userContact;
    private String lastMessage;
    private LocalDateTime lastTime;
    private boolean isCurrentUserLastMessage;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntityDTO getUserContact() {
        return userContact;
    }

    public void setUserContact(UserEntityDTO userContact) {
        this.userContact = userContact;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isCurrentUserLastMessage() {
        return isCurrentUserLastMessage;
    }

    public void setCurrentUserLastMessage(boolean currentUserLastMessage) {
        isCurrentUserLastMessage = currentUserLastMessage;
    }
}
