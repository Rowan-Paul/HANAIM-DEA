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

//TODO: check status codes
@Path("/playlists")
public class PlaylistService {
    private IPlaylistDAO playlistDAO;
    private IUserDAO userDAO;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists(@QueryParam("token") String token) {
        User user = userDAO.verifyToken(token);

        return Response.status(200).entity(getAllPlaylistsList(user)).build();
    }

    public PlaylistsDTO getAllPlaylistsList(User user) {
        ArrayList playlists = playlistDAO.getPlaylists();
        PlaylistsDTO playlistsDTO = new PlaylistsDTO();
        playlistsDTO.playlists = new ArrayList<>();
        playlistsDTO.length = 69;

        playlists.forEach((playlist) -> {
            Playlist pl = (Playlist) playlist;

            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.id = pl.getId();
            playlistDTO.name = pl.getName();
            playlistDTO.tracks = pl.getTracks();
            playlistDTO.length = 69;

            if (pl.getOwner().equals(user.getUser())) {
                playlistDTO.owner = true;
            } else {
                playlistDTO.owner = false;
            }

            playlistsDTO.playlists.add(playlistDTO);
        });

        return playlistsDTO;
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(PlaylistDTO newPlaylistDTO, @QueryParam("token") String token) {
        User user = userDAO.verifyToken(token);

        if (playlistDAO.createPlaylist(newPlaylistDTO, user.getUser())) {
            return Response.status(201).entity(getAllPlaylistsList(user)).build();
        }

        return Response.status(400).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(@PathParam("id") int playlistid, PlaylistDTO newPlaylistDTO, @QueryParam("token") String token) {
        User user = userDAO.verifyToken(token);

        if (playlistDAO.editPlaylist(playlistid, newPlaylistDTO)) {
            return Response.status(200).entity(getAllPlaylistsList(user)).build();
        }

        return Response.status(400).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int playlistid, @QueryParam("token") String token) {
        User user = userDAO.verifyToken(token);

        if (playlistDAO.deletePlaylist(playlistid)) {
            return Response.status(200).entity(getAllPlaylistsList(user)).build();
        } else {
            return Response.status(400).build();
        }
    }

    @DELETE
    @Path("/{playlistid}/tracks/{trackid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTrackInPlaylist(@PathParam("playlistid") int playlistid, @PathParam("trackid") int trackid, @QueryParam("token") String token) {
        User user = userDAO.verifyToken(token);

        if (playlistDAO.deleteTrackInPLaylist(playlistid, trackid)) {
            ArrayList<Track> tracks = playlistDAO.getTracksFromPlaylist(playlistid);

            //TODO: fix deleting last track giving 400
            return Response.status(200).entity(tracks).build();
        }

        return Response.status(400).build();
    }

    @POST
    @Path("/{playlistid}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(@PathParam("playlistid") int playlistid, @QueryParam("token") String token, TrackDTO trackDTO) {
        User user = userDAO.verifyToken(token);

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
        User user = userDAO.verifyToken(token);

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
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
