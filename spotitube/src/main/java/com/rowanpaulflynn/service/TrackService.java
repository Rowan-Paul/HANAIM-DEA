package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.ITrackDAO;
import com.rowanpaulflynn.service.dto.TracksListDTO;

import javax.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

//TODO: Only return tracks that aren't in
//      the playlist already
@Path("/tracks")
public class TrackService {

    private ITrackDAO trackDAO;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracks() {
        // Retrieve user from database
        ArrayList tracks = trackDAO.getTracks();

        if (tracks.size() < 1) {
            return Response.status(404).build();
        }

        TracksListDTO tracksListDTO = new TracksListDTO();
        tracksListDTO.tracks = tracks;


        return Response.status(200).entity(tracksListDTO).build();
    }

    @Inject
    public void setTrackDAO(ITrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }
}