package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.PlaylistDTO;
import com.rowanpaulflynn.service.dto.PlaylistsDTO;
import com.rowanpaulflynn.service.dto.TrackDTO;
import com.rowanpaulflynn.service.dto.TracklistDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/playlists")
public class PlaylistService {
    private IPlaylistDAO playlistDAO;
    private IUserDAO userDAO;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists(@QueryParam("token") String token) {
        if (token == null) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        return Response.status(200).entity(getAllPlaylistsList(user)).build();
    }

    public PlaylistsDTO getAllPlaylistsList(User user) {
        ArrayList playlists = playlistDAO.getPlaylists();
        PlaylistsDTO playlistsDTO = new PlaylistsDTO();
        playlistsDTO.playlists = new ArrayList<>();

        int length = 0;

        for (Object playlist : playlists) {
            Playlist pl = (Playlist) playlist;

            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.id = pl.getId();
            playlistDTO.name = pl.getName();
            playlistDTO.tracks = pl.getTracks();
            playlistDTO.length = pl.getLength();
            length += pl.getLength();

            if (pl.getOwner().equals(user.getUser())) {
                playlistDTO.owner = true;
            } else {
                playlistDTO.owner = false;
            }

            playlistsDTO.playlists.add(playlistDTO);
        }
        ;

        playlistsDTO.length = length;

        return playlistsDTO;
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(PlaylistDTO newPlaylistDTO, @QueryParam("token") String token) {
        if (token == null || newPlaylistDTO == null) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        Playlist playlist = new Playlist(newPlaylistDTO.name, user.getUser());
        playlist.setId(newPlaylistDTO.id);
        playlist.setTracks(newPlaylistDTO.tracks);
        playlist.setLength(newPlaylistDTO.length);

        boolean playlistCreated = playlistDAO.createPlaylist(playlist, user.getUser());

        if (playlistCreated) {
            return Response.status(200).entity(getAllPlaylistsList(user)).build();
        }
        return Response.status(400).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPlaylistTracks(@PathParam("id") int playlistid, PlaylistDTO newPlaylistDTO, @QueryParam("token") String token) {
        if (token == null || playlistid < 1 || newPlaylistDTO == null || token == null) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        Playlist playlist = new Playlist(newPlaylistDTO.name, user.getUser());
        playlist.setId(newPlaylistDTO.id);
        playlist.setTracks(newPlaylistDTO.tracks);
        playlist.setLength(newPlaylistDTO.length);

        if (playlistDAO.editPlaylist(playlistid, playlist)) {
            return Response.status(200).entity(getAllPlaylistsList(user)).build();
        }

        return Response.status(400).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int playlistid, @QueryParam("token") String token) {
        if (token == null || playlistid < 1) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        if (playlistDAO.deletePlaylist(playlistid)) {
            return Response.status(200).entity(getAllPlaylistsList(user)).build();
        }
        return Response.status(400).build();
    }

    @DELETE
    @Path("/{playlistid}/tracks/{trackid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTrackInPlaylist(@PathParam("playlistid") int playlistid, @PathParam("trackid") int trackid, @QueryParam("token") String token) {
        if (token == null || playlistid < 1 || trackid < 1) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        if (playlistDAO.deleteTrackInPlaylist(playlistid, trackid)) {
            ArrayList<Track> tracks = playlistDAO.getTracksFromPlaylist(playlistid);

            return Response.status(200).entity(tracks).build();
        }
        return Response.status(400).build();
    }

    @POST
    @Path("/{playlistid}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("playlistid") int playlistid, @QueryParam("token") String token, TrackDTO trackDTO) {
        if (token == null || playlistid < 1 || trackDTO == null) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        if (playlistDAO.addTrackToPlaylist(playlistid, trackDTO.id)) {
            ArrayList<Track> tracks = playlistDAO.getTracksFromPlaylist(playlistid);

            return Response.status(201).entity(tracks).build();
        }

        return Response.status(400).build();
    }

    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTracks(@QueryParam("token") String token, @PathParam("id") int playlistid) {
        if (token == null || playlistid < 1) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        TracklistDTO tracklistDTO = new TracklistDTO();
        tracklistDTO.tracks = playlistDAO.getTracksFromPlaylist(playlistid);

        if (tracklistDTO.tracks.size() > 0) {
            return Response.status(200).entity(tracklistDTO).build();
        }
        return Response.status(404).build();
    }

    @Inject
    public void setPlaylistDAO(IPlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    @Inject
    public void setUserDAO(IUserDAO userDAOMongo) {
        this.userDAO = userDAOMongo;
    }
}
