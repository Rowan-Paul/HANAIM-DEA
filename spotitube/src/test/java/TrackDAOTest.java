import com.rowanpaulflynn.dao.TrackDAO;
import com.rowanpaulflynn.domain.Track;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackDAOTest {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    TrackDAO trackDAO = new TrackDAO();

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

            // setup classes
            trackDAO.setDataSource(dataSource);

            // Act
            ArrayList<Track> tracks = trackDAO.getTracks();

            // Assert
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement.executeQuery());

            assertEquals(expectedId, tracks.get(0).getId());
            assertEquals(expectedTitle, tracks.get(0).getTitle());
            assertEquals(expectedPerformer, tracks.get(0).getPerformer());
        } catch (Exception e) {
            fail();
            e.getMessage();
        }
    }
}