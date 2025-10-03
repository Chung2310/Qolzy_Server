package com.example.Qolzy.model.music;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idM;
    private String id;
    private String name;
    private int duration;
    private String artistId;
    private String artistName;
    private String albumName;
    private String releaseDate;
    private String licenseUrl;
    private String audioUrl;
    private String audioDownload;
    private boolean audioDownloadAllowed;
    private String imageUrl;

    public Long getIdM() {
        return idM;
    }

    public void setIdM(Long idM) {
        this.idM = idM;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getAudioDownload() {
        return audioDownload;
    }

    public void setAudioDownload(String audioDownload) {
        this.audioDownload = audioDownload;
    }

    public boolean isAudioDownloadAllowed() {
        return audioDownloadAllowed;
    }

    public void setAudioDownloadAllowed(boolean audioDownloadAllowed) {
        this.audioDownloadAllowed = audioDownloadAllowed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
