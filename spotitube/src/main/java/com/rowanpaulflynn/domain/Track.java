package com.rowanpaulflynn.domain;

public class Track {
    private int id;
    private String title;
    private String performer;
    private int duration;
    private String album;
    private int playcount;
    private String publicationDate;
    private String description;
    private boolean offlineAvailable;

    public Track(int id, String title, String performer) {
        this.id = id;
        this.title = title;
        this.performer = performer;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPerformer() {
        return performer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }
}
