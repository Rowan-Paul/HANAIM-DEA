package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.exceptions.AccessDeniedError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOTest {
    private UserDAO userDAO;
    private DataSource dataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setup() {
        dataSource = mock(DataSource.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        userDAO = new UserDAO();
        userDAO.setDataSource(dataSource);
    }

    /**
     * getUser()
     * */
    @Test
    public void getUserTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from users where user = ?";
            final String username = "rowan";

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getString("user")).thenReturn(username);

            // Act
            User user = userDAO.getUser(username);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, username);

            assertEquals(username, user.getUser());
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void getUserFailedTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from users where user = ?";
            final String username = "rowan";

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            when(resultSet.getString("user")).thenReturn(username);

            // Act
            User user = userDAO.getUser(username);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, username);

            assertEquals(null, user);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void getUserTrowsErrorTest() {
        try {
            final String expectedSQL = "select * from users where user = ?";
            final String username = "rowan";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenThrow(new SQLException());

            assertThrows(AccessDeniedError.class, () -> {
                userDAO.getUser(username);
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * createToken()
     * */
    @Test
    public void createTokenTest() {
        try {
            // Arrange
            final String expectedSQL = "insert into tokens (`token`, `user`) values (?, ?)";
            final String username = "rowan";

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            // Act
            Token token = userDAO.createToken(username);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, token.getToken());
            verify(preparedStatement).setString(2, token.getUser());

            assertEquals(username, token.getUser());
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void createTokenTrowsErrorTest() {
        try {
            final String expectedSQL = "insert into tokens (`token`, `user`) values (?, ?)";
            final String username = "rowan";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            assertThrows(AccessDeniedError.class, () -> {
                userDAO.createToken(username);
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * verifyToken()
     * */
    @Test
    public void verifyTokenTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from tokens where token = ?";
            final String username = "rowan";
            final String expectedToken = "abb94ad7-1e79-43a7-a726-dc014e202351";

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getString("user")).thenReturn(username);

            // Act
            User user = userDAO.verifyToken(expectedToken);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, expectedToken);

            assertEquals(username, user.getUser());
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }
    @Test
    public void verifyTokenFailedTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from tokens where token = ?";
            final String username = "rowan";
            final String expectedToken = "abb94ad7-1e79-43a7-a726-dc014e202351";

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            when(resultSet.getString("user")).thenReturn(username);

            // Act
            User user = userDAO.verifyToken(expectedToken);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, expectedToken);

            assertEquals(null, user);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void verifyTokenTrowsErrorTest() {
        try {
            final String expectedSQL = "select * from tokens where token = ?";
            final String expectedToken = "abb94ad7-1e79-43a7-a726-dc014e202351";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenThrow(new SQLException());

            assertThrows(AccessDeniedError.class, () -> {
                userDAO.verifyToken(expectedToken);
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }
}