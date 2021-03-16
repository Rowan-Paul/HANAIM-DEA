package com.rowanpaulflynn.domain;

import java.util.ArrayList;

public class Playlist {
    private int id;
    private String name;
    private String owner;
    private ArrayList tracks;

    public Playlist(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public ArrayList getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList tracks) {
        this.tracks = tracks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
