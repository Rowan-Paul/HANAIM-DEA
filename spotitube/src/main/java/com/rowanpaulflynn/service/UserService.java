package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.UserDTO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class UserService {

    private IUserDAO userDAO;

    /**
     * Obviously a test, never send back a password!
     * */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabase(@PathParam("id") String id) {
        // Retrieve from database:
        User user = userDAO.getUser(id);

        if (user == null){
            return Response.status(404).build();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.user = user.getUser();
        userDTO.password = user.getPassword();

        return Response.status(200).entity(userDTO).build();

    }

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
