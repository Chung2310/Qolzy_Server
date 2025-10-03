package com.example.Qolzy.model.post;

import com.example.Qolzy.dto.PostMediaDTO;
import com.example.Qolzy.dto.UserEntityDTO;
import com.example.Qolzy.model.music.Music;

import java.time.LocalDateTime;
import java.util.List;

public class PostReponse {
    private Long id;
    private UserEntityDTO user;
    private int likes;
    private String content;
    private int comments;
    private Music music;
    private Boolean likedByCurrentUser;
    private List<PostMediaDTO> medias;
    private LocalDateTime createAt;

    public Boolean getLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(Boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public List<PostMediaDTO> getMedias() {
        return medias;
    }

    public void setMedias(List<PostMediaDTO> medias) {
        this.medias = medias;
    }
}
