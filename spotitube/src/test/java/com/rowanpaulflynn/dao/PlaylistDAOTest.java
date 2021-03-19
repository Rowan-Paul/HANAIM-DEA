package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.exceptions.InternalServerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlaylistDAOTest {
    private PlaylistDAO playlistDAO;
    private PlaylistDAO mockPlaylistDAO;
    private DataSource dataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private ArrayList<Track> expectedTracks = new ArrayList<>();
    private Track expectedTrack1 = new Track(1,"the 1","Taylor Swift");
    private Track expectedTrack2 = new Track(2,"coney island","Taylor Swift");

    @BeforeEach
    public void setup() {
        dataSource = mock(DataSource.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        playlistDAO = new PlaylistDAO();
        mockPlaylistDAO = Mockito.spy(playlistDAO);
        mockPlaylistDAO.setDataSource(dataSource);

        expectedTrack1.setDuration(200);
        expectedTracks.add(expectedTrack1);
        expectedTrack2.setDuration(300);
        expectedTracks.add(expectedTrack2);
    }

    @Test
    public void getPlaylistsTest() {
        try {
            final String expectedSQL = "select * from playlists";
            final int expectedId = 1;
            final String expectedName = "Folklore era";
            final String expectedOwner = "rowan";
            final int expectedLength = 500;

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("id")).thenReturn(expectedId);
            when(resultSet.getString("owner")).thenReturn(expectedOwner);
            when(resultSet.getString("name")).thenReturn(expectedName);
            when(resultSet.getInt("length")).thenReturn(expectedLength);;
            when(resultSet.getString("tracks")).thenReturn(String.valueOf(expectedTracks));

            Mockito.doReturn(expectedTracks).when(mockPlaylistDAO).getTracksFromPlaylist(expectedId);

            ArrayList<Playlist> playlists = mockPlaylistDAO.getPlaylists();

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);

            assertEquals(expectedId, playlists.get(0).getId());
            assertEquals(expectedName, playlists.get(0).getName());
            assertEquals(expectedOwner, playlists.get(0).getOwner());
            assertEquals(expectedLength, playlists.get(0).getLength());
            assertEquals(expectedTracks,playlists.get(0).getTracks());
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void getAllPlaylistsTrowsError() {
        try {
            final String expectedSQL = "select * from playlists";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenThrow(new SQLException());

            assertThrows(InternalServerError.class, () -> {
                mockPlaylistDAO.getPlaylists();
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Test
    public void calculatePlaylistLengthTest() {
        try {
            int expectedLength = 500;

            int length = playlistDAO.calculatePlaylistLength(expectedTracks);

            assertEquals(expectedLength, length);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void getTracksFromPlaylistTest() {
        try {
            final String expectedSQL = "select * from playlisttracks where playlistid = ?";
            final int expectedId = 1;

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("trackid")).thenReturn(expectedId);

            Mockito.doReturn(expectedTrack1).when(mockPlaylistDAO).getTrackInfo(1);
            Mockito.doReturn(expectedTrack2).when(mockPlaylistDAO).getTrackInfo(2);

            ArrayList<Track> tracks = mockPlaylistDAO.getTracksFromPlaylist(expectedId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, expectedId);

            assertEquals(expectedTrack1, tracks.get(0));
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void getTracksInfoTest() {
        try {
            final String expectedSQL = "select * from tracks where id = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("trackid")).thenReturn(expectedTrack1.getId());
            when(resultSet.getString("title")).thenReturn(expectedTrack1.getTitle());
            when(resultSet.getString("performer")).thenReturn(expectedTrack1.getPerformer());

            Track track = mockPlaylistDAO.getTrackInfo(expectedTrack1.getId());

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, expectedTrack1.getId());

            assertEquals(expectedTrack1.getTitle(), track.getTitle());
            assertEquals(expectedTrack1.getId(), track.getId());
            assertEquals(expectedTrack1.getPerformer(), track.getPerformer());
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void deletePlaylistTest() {
        try {
            final String expectedSQL = "delete from playlists where id = ?";
            final int expectedId = 1;

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            Mockito.doReturn(true).when(mockPlaylistDAO).deleteTracksInPlaylist(expectedId);

            boolean deletePlaylist = mockPlaylistDAO.deletePlaylist(expectedId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, expectedId);

            assertTrue(deletePlaylist);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void createPlaylistTest() {
        try {
            final String expectedSQL = "insert into playlists (`name`, `owner`) values (?, ?)";
            int expectedId = 1;
            final String expectedOwner = "rowan";
            final String expectedName = "newplaylist";
            Playlist expectedPlaylist = new Playlist(expectedName,expectedOwner);
            expectedPlaylist.setTracks(expectedTracks);

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            Mockito.doReturn(expectedId).when(mockPlaylistDAO).getPlaylistIdFromName(expectedName);
            Mockito.doReturn(true).when(mockPlaylistDAO).addTrackToPlaylist(expectedId, expectedTrack1.getId());
            Mockito.doReturn(true).when(mockPlaylistDAO).addTrackToPlaylist(expectedId, expectedTrack2.getId());

            boolean createPlaylist = mockPlaylistDAO.createPlaylist(expectedPlaylist,expectedOwner);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, expectedName);
            verify(preparedStatement).setString(2, expectedOwner);

            assertTrue(createPlaylist);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void getPlaylistIdFromNameTest() {
        try {
            final String expectedSQL = "select * from playlists where name = ?";
            final int expectedId = 1;
            final String expectedName = "playlist";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("id")).thenReturn(expectedId);

            int playlistid = mockPlaylistDAO.getPlaylistIdFromName(expectedName);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, expectedName);

            assertEquals(expectedId, playlistid);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void addTrackToPlaylistTest() {
        try {
            final String expectedSQL = "insert into playlisttracks (`playlistid`, `trackid`) values (?, ?)";
            int expectedPlaylistId = 1;
            int expectedTrackId = 2;

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean addTrackToPlaylist = mockPlaylistDAO.addTrackToPlaylist(expectedPlaylistId, expectedTrackId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, expectedPlaylistId);
            verify(preparedStatement).setInt(2, expectedTrackId);

            assertTrue(addTrackToPlaylist);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void editPlaylistTest() {
        try {
            final String expectedSQL = "update playlists set name = ? where id = ?";
            final int expectedId = 1;
            final String expectedOwner = "rowan";
            final String expectedName = "newplaylist";
            Playlist expectedPlaylist = new Playlist(expectedName,expectedOwner);
            expectedPlaylist.setTracks(expectedTracks);

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("id")).thenReturn(expectedId);
            Mockito.doReturn(true).when(mockPlaylistDAO).deleteTracksInPlaylist(expectedId);
            Mockito.doReturn(true).when(mockPlaylistDAO).addTrackToPlaylist(expectedId, expectedTrack1.getId());
            Mockito.doReturn(true).when(mockPlaylistDAO).addTrackToPlaylist(expectedId, expectedTrack2.getId());

            boolean editPlaylist= mockPlaylistDAO.editPlaylist(expectedId,expectedPlaylist);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, expectedName);
            verify(preparedStatement).setInt(2, expectedId);

            assertTrue(editPlaylist);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void deleteTracksInPlaylistTest() {
        try {
            final String expectedSQL = "delete from playlisttracks where playlistid = ?";
            int expectedPlaylistId = 1;

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean deleteTracksInPlaylist = mockPlaylistDAO.deleteTracksInPlaylist(expectedPlaylistId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, expectedPlaylistId);

            assertTrue(deleteTracksInPlaylist);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void deleteTrackInPlaylistTest() {
        try {
            final String expectedSQL = "delete from playlisttracks where playlistid = ? and trackid = ?";
            int expectedPlaylistId = 1;
            int expectedTrackId = 2;

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean deleteTrackInPlaylist = mockPlaylistDAO.deleteTrackInPlaylist(expectedPlaylistId, expectedTrackId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, expectedPlaylistId);
            verify(preparedStatement).setInt(2, expectedTrackId);

            assertTrue(deleteTrackInPlaylist);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }
}