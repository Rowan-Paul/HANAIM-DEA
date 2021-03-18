package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.UserService;
import com.rowanpaulflynn.service.dto.TokenDTO;
import com.rowanpaulflynn.service.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    /**
     * Happy path
     * POST /login
     * */
    @Test
    public void loginUserTest() {
        // Arrange
        int statuscodeExpected = 201;
        final String username = "rowan";
        User user = new User(username);
        user.setPassword("password");

        User returnedUser = new User(username);
        returnedUser.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");

        Token token = new Token(user.getUser());

        UserDTO userDTO = new UserDTO();
        userDTO.user = user.getUser();
        userDTO.password = user.getPassword();


        IUserDAO userDAOMock = mock(IUserDAO.class);
        when(userDAOMock.getUser(user.getUser())).thenReturn(returnedUser);
        when(userDAOMock.createToken(user.getUser())).thenReturn(token);
        userService.setUserDAO(userDAOMock);

        // Act
        Response response = userService.loginUser(userDTO);
        TokenDTO tokenDTO = (TokenDTO) response.getEntity();
        
        // Assert
        assertEquals(statuscodeExpected, response.getStatus());
        assertEquals(username,tokenDTO.user);
        assertEquals(token.getToken(), tokenDTO.token);
    }


    /**
     * Unhappy path - user has the wrong password
     * POST /login
     * */
    @Test
    public void loginUserFailedTest() {
        // Arrange
        int statuscodeExpected = 401;
        final String username = "rowan";
        final String password = "wrongpassword";
        final String hashedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        User user = new User(username);
        user.setPassword(password);

        User returnedUser = new User(username);
        returnedUser.setPassword(hashedPassword);

        UserDTO userDTO = new UserDTO();
        userDTO.user = user.getUser();
        userDTO.password = user.getPassword();


        IUserDAO userDAOMock = mock(IUserDAO.class);
        when(userDAOMock.getUser(user.getUser())).thenReturn(returnedUser);
        userService.setUserDAO(userDAOMock);

        // Act
        Response response = userService.loginUser(userDTO);

        // Assert
        assertEquals(statuscodeExpected, response.getStatus());
    }
}