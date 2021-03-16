package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.service.dto.PlaylistDTO;

import java.util.ArrayList;

public interface IPlaylistDAO {
    ArrayList<Playlist> getPlaylists();
    ArrayList<Track> getTracksFromPlaylist(int playlistid);
    Track getTrackInfo(int track);
    Boolean deletePlaylist(int playlistid);
    Boolean createPlaylist(PlaylistDTO playlistDTO, String owner);
    Boolean addTrackToPlaylist(int playlistid, int trackid);
    int getPlaylistIdFromName(String playlistname);
    Boolean editPlaylist(int playlistid, PlaylistDTO playlistDTO);
    Boolean deleteTracksInPLaylist(int playlistid);
}
