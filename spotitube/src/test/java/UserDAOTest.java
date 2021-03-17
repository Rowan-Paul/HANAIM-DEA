import com.rowanpaulflynn.dao.UserDAO;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.User;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOTest {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    UserDAO userDAO = new UserDAO();

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

            // setup classes
            userDAO.setDataSource(dataSource);

            // Act
            User user = userDAO.getUser(username);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, username);

            assertEquals(username, user.getUser());
        } catch (Exception e) {
            fail();
            e.getMessage();
        }
    }

    @Test
    public void getUserWrongStatementTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from users where dog = ?";
            final String username = "rowan";

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                        User user = userDAO.getUser(username);
                    }
            );
        } catch (Exception e) {
            fail();
            e.getMessage();
        }
    }

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
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            userDAO.setDataSource(dataSource);

            // Act
            Token token = userDAO.createToken(username);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, token.getToken());
            verify(preparedStatement).setString(2, token.getUser());

            assertEquals(username, token.getUser());
        } catch (Exception e) {
            fail();
            e.getMessage();
        }
    }

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

            userDAO.setDataSource(dataSource);

            // Act
            User user = userDAO.verifyToken(expectedToken);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, expectedToken);

            assertEquals(username, user.getUser());
        } catch (Exception e) {
            fail();
            e.getMessage();
        }
    }
}