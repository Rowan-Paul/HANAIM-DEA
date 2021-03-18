package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.dao.TrackDAO;
import com.rowanpaulflynn.domain.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackDAOTest {
    private TrackDAO trackDAO;
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

        trackDAO = new TrackDAO();
        trackDAO.setDataSource(dataSource);
    }

    @Test
    public void getTracksTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from tracks";
            final int expectedId = 1;
            String expectedTitle = "the 1";
            String expectedPerformer = "Taylor Swift";

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("id")).thenReturn(expectedId);
            when(resultSet.getString("title")).thenReturn(expectedTitle);
            when(resultSet.getString("performer")).thenReturn(expectedPerformer);

            // Act
            ArrayList<Track> tracks = trackDAO.getTracks();

            // Assert
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);

            assertEquals(expectedId, tracks.get(0).getId());
            assertEquals(expectedTitle, tracks.get(0).getTitle());
            assertEquals(expectedPerformer, tracks.get(0).getPerformer());
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }
}