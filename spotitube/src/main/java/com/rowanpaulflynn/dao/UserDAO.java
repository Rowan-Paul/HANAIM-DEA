package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.User;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Default
public class UserDAO implements IUserDAO {
    @Resource(name="jdbc/spotitube")
    DataSource dataSource;

    @Override
    public User getUser(String inputUser) {
        String sql = "select * from users where user = ?";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,inputUser);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                User user = new User(resultSet.getString("user"));
                user.setUser(resultSet.getString("user"));
                user.setPassword(resultSet.getString("password"));

                return user;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public Token createToken(String user) {
        String sql = "INSERT INTO tokens (`token`, `user`) VALUES (?, ?)";

        try(Connection connection = dataSource.getConnection();) {
            Token token = new Token(user);

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(2,user);
            statement.setString(1,token.getToken());
            int resultSet = statement.executeUpdate();

            return token;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
