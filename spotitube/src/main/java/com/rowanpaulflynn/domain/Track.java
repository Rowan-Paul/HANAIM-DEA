package com.rowanpaulflynn.domain;

public class Track {
    private int id;
    private String title;
    private String performer;
    private String duration;
    private String album;
    private String playcount;
    private String publicationDate;
    private String description;
    private String offlineAvailable;

    public Track(int id, String title, String performer) {
        this.id = id;
        this.title = title;
        this.performer = performer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(String offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }
}
