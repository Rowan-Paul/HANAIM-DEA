package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.TokenDTO;
import com.rowanpaulflynn.service.dto.UserDTO;

import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class UserService {
    private IUserDAO userDAO;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(UserDTO userDTO) {
        if(userDTO == null) {
            return Response.status(400).build();
        }
        // Retrieve user from database
        User user = userDAO.getUser(userDTO.user);

        if(user == null) {
            return Response.status(401).build();
        }

        // Check stored hashed password with hashed input password
        if(DigestUtils.sha256Hex(userDTO.password).equals(user.getPassword())) {
            Token token = userDAO.createToken(userDTO.user);

            if(token == null) {
                return Response.status(400).build();
            }

            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.token = token.getToken();
            tokenDTO.user = token.getUser();

            return Response.status(201).entity(tokenDTO).build();
        } else {
            return Response.status(401).build();
        }
    }

    @Inject
    public void setUserDAO(IUserDAO userDAOMongo) {
        this.userDAO = userDAOMongo;
    }
}
