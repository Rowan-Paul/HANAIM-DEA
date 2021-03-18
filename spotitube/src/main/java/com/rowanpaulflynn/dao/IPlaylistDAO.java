package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;

import java.util.ArrayList;

public interface IPlaylistDAO {
    ArrayList<Playlist> getPlaylists();
    int calculatePlaylistLength(ArrayList<Track> tracks);
    ArrayList<Track> getTracksFromPlaylist(int playlistid);
    Track getTrackInfo(int track);
    Boolean deletePlaylist(int playlistid);
    Boolean createPlaylist(Playlist playlist, String owner);
    Boolean addTrackToPlaylist(int playlistid, int trackid);
    int getPlaylistIdFromName(String playlistname);
    Boolean editPlaylist(int playlistid, Playlist playlist);
    Boolean deleteTracksInPlaylist(int playlistid);
    Boolean deleteTrackInPlaylist(int playlistid, int trackid);
}
