package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Track;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

@Default
public class TrackDAO implements ITrackDAO {
    @Resource(name="jdbc/spotitube")
    DataSource dataSource;

    @Override
    public ArrayList<Track> getTracks() {
        String sql = "select * from tracks";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Track> tracks = new ArrayList<Track>();

            while (resultSet.next()){
                Track track = new Track(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("performer"));

                tracks.add(track);
            }

            return tracks;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
