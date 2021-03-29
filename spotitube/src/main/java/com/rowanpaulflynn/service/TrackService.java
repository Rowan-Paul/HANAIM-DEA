package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.ITrackDAO;
import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.TracklistDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/tracks")
public class TrackService {

    IUserDAO userDAO;
    IPlaylistDAO playlistDAO;
    private ITrackDAO trackDAO;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTracks(@QueryParam("token") String token, @QueryParam("forPlaylist") int playlistid) {
        if (token == null || playlistid < 1) {
            return Response.status(400).build();
        }
        User user = userDAO.verifyToken(token);
        if (user == null) {
            return Response.status(401).build();
        }

        ArrayList<Track> inPlaylist = playlistDAO.getTracksFromPlaylist(playlistid);
        ArrayList<Track> allTracks = trackDAO.getTracks();

        if (inPlaylist.size() > 0) {
            ArrayList<Track> tracks = new ArrayList<>();
            ArrayList<Integer> playlistTrackIDs = new ArrayList<>();

            for (Track track : inPlaylist) {
                playlistTrackIDs.add(track.getId());
            }

            for (Track track : allTracks) {
                if(!playlistTrackIDs.contains(track.getId())) {
                    tracks.add(track);
                }
            }

            TracklistDTO tracklistDTO = new TracklistDTO();
            tracklistDTO.tracks = tracks;
            return Response.status(200).entity(tracklistDTO).build();
        } else {
            TracklistDTO tracklistDTO = new TracklistDTO();
            tracklistDTO.tracks = allTracks;
            return Response.status(200).entity(tracklistDTO).build();
        }
    }

    @Inject
    public void setTrackDAO(ITrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Inject
    public void setPlaylistDAO(IPlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }
}