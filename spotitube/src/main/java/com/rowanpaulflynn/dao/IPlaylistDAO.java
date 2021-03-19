package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;

import java.util.ArrayList;

public interface IPlaylistDAO {
    ArrayList<Playlist> getPlaylists();
    int calculatePlaylistLength(ArrayList<Track> tracks);
    ArrayList<Track> getTracksFromPlaylist(int playlistid);
    Track getTrackInfo(int track);
    boolean deletePlaylist(int playlistid);
    boolean createPlaylist(Playlist playlist, String owner);
    boolean addTrackToPlaylist(int playlistid, int trackid);
    int getPlaylistIdFromName(String playlistname);
    boolean editPlaylist(int playlistid, Playlist playlist);
    boolean deleteTracksInPlaylist(int playlistid);
    boolean deleteTrackInPlaylist(int playlistid, int trackid);
}
