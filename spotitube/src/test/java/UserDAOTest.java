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
    @Test
    public void getUserTest() {
        try {
            // Arrange
            final String expectedSQL = "select * from users where user = ?";
            final String username = "rowan";

            // setup mocks
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement preparedStatement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            // setup classes
            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);

            // Act
            User user = userDAO.getUser(username);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, username);

            assertNull(user);
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

            // setup mocks
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement preparedStatement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            // setup classes
            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);

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
            final String expectedSQL = "INSERT INTO tokens (`token`, `user`) VALUES (?, ?)";
            final String username = "rowan";

            // setup mocks
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement preparedStatement = mock(PreparedStatement.class);

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            // setup classes
            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);

            // Act
            Token token = userDAO.createToken(username);

            // Assert
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(2, username);
        } catch (Exception e) {
            fail();
            e.getMessage();
        }
    }

    @Test
    public void createTokenWrongStatementTest() {
        try {
            // Arrange
            final String expectedSQL = "INSERT INTO token (`token`, `user`) VALUES (?, ?)";
            final String username = "rowan";

            // setup mocks
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement preparedStatement = mock(PreparedStatement.class);

            // instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            // setup classes
            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);


            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                        Token token = userDAO.createToken(username);
                    }
            );
        } catch (Exception e) {
            fail();
            e.getMessage();
        }
    }
}