package com.example.Qolzy.model.contact;

import com.example.Qolzy.model.auth.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    private UserEntity user;

    @ManyToOne(fetch =  FetchType.LAZY)
    private UserEntity userContact;

    private String lastMessage;
    private LocalDateTime lastTime;

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

    public UserEntity getUserContact() {
        return userContact;
    }

    public void setUserContact(UserEntity userContact) {
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
}
