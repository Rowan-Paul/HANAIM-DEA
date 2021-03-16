package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.PlaylistDTO;
import com.rowanpaulflynn.service.dto.TrackDTO;

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
        User user = userDAO.verifyToken(token);

        ArrayList playlists = playlistDAO.getPlaylists();

        if (playlists.size() < 1) {
            return Response.status(404).build();
        }

        ArrayList<PlaylistDTO> playlistsDTO = new ArrayList<>();

        playlists.forEach((playlist) -> {
            Playlist pl = (Playlist) playlist;

            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.id = pl.getId();
            playlistDTO.name = pl.getName();
            playlistDTO.tracks = pl.getTracks();

            if (pl.getOwner().equals(user.getUser())) {
                playlistDTO.owner = true;
            } else {
                playlistDTO.owner = false;
            }

            playlistsDTO.add(playlistDTO);
        });

        return Response.status(200).entity(playlistsDTO).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(PlaylistDTO newPlaylistDTO, @QueryParam("token") String token) {
        User user = userDAO.verifyToken(token);

        if (playlistDAO.createPlaylist(newPlaylistDTO, user.getUser())) {
            return getAllPlaylists(token);
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
            return getAllPlaylists(token);
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
            return getAllPlaylists(token);
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
            //TODO: return tracks from playlist
            return Response.status(200).build();
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
            //TODO: return tracks from playlist
            return Response.status(201).build();
        }

        return Response.status(400).build();
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
