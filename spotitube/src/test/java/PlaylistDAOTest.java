import com.rowanpaulflynn.dao.PlaylistDAO;
import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlaylistDAOTest {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    PlaylistDAO playlistDAO = new PlaylistDAO();
    PlaylistDAO mockPlaylistDAO = Mockito.spy(playlistDAO);

    //TODO: make this test work when executing all tests
    @Test
    public void getPlaylistsTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from playlists";
            final int expectedId = 1;
            String expectedName = "Folklore era";
            String expectedOwner = "rowan";
            int expectedLength = 500;
            ArrayList<Track> tracks = new ArrayList<>();

            Track tk1 = new Track(1,"the 1","Taylor Swift");
            tk1.setDuration(200);
            tracks.add(tk1);

            Track tk2 = new Track(2,"coney island","Taylor Swift");
            tk2.setDuration(300);
            tracks.add(tk2);


            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("id")).thenReturn(expectedId);
            when(resultSet.getString("owner")).thenReturn(expectedOwner);
            when(resultSet.getString("name")).thenReturn(expectedName);
            when(resultSet.getInt("length")).thenReturn(expectedLength);;
            when(resultSet.getString("tracks")).thenReturn(String.valueOf(tracks));

            mockPlaylistDAO.setDataSource(dataSource);

            Mockito.doReturn(tracks).when(mockPlaylistDAO).getTracksFromPlaylist(expectedId);
            ArrayList<Playlist> playlists = mockPlaylistDAO.getPlaylists();

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement.executeQuery());

            assertEquals(expectedId, playlists.get(0).getId());
            assertEquals(expectedName, playlists.get(0).getName());
            assertEquals(expectedOwner, playlists.get(0).getOwner());
            assertEquals(expectedLength, playlists.get(0).getLength());
            assertEquals(tracks,playlists.get(0).getTracks());
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }

    @Test
    public void calculatePlaylistLengthTest() {
        try {
            int expectedLength = 500;
            ArrayList<Track> tracks = new ArrayList<>();

            Track tk1 = new Track(1,"the 1","Taylor Swift");
            tk1.setDuration(200);
            tracks.add(tk1);

            Track tk2 = new Track(2,"coney island","Taylor Swift");
            tk2.setDuration(300);
            tracks.add(tk2);

            int length = playlistDAO.calculatePlaylistLength(tracks);

            assertEquals(expectedLength, length);
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }


    @Test
    public void getTracksFromPlaylistTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from playlisttracks where playlistid = ?";
            int expectedId = 1;
            ArrayList<Track> expectedTracks = new ArrayList<>();

            Track expectedTrack1 = new Track(1,"the 1","Taylor Swift");
            expectedTrack1.setDuration(200);
            expectedTracks.add(expectedTrack1);

            Track expectedTrack2 = new Track(2,"coney island","Taylor Swift");
            expectedTrack2.setDuration(300);
            expectedTracks.add(expectedTrack2);


            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            when(resultSet.getInt("trackid")).thenReturn(expectedId);

            mockPlaylistDAO.setDataSource(dataSource);

            Mockito.doReturn(expectedTrack1).when(mockPlaylistDAO).getTrackInfo(1);
            Mockito.doReturn(expectedTrack2).when(mockPlaylistDAO).getTrackInfo(2);

            ArrayList<Track> tracks = mockPlaylistDAO.getTracksFromPlaylist(expectedId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement.executeQuery());

            assertEquals(expectedTrack1, tracks.get(0));
        } catch (Exception e) {
            fail(e);
            e.getMessage();
        }
    }
}