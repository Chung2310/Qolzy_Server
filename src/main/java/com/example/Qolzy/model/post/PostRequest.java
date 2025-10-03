package com.example.Qolzy.model.post;

import com.example.Qolzy.model.music.Music;

public class PostRequest {
    private Long id;
    private String content;
    private Music music;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

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

}
