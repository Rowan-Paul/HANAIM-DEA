package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.exceptions.InternalServerError;

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
    public ArrayList<Track> getTracks() throws InternalServerError  {
        String sql = "select * from tracks";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<Track>();

            while (resultSet.next()){
                Track track = new Track(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("performer"));
                track.setAlbum(resultSet.getString("album"));
                track.setDuration(resultSet.getInt("duration"));
                track.setPlaycount(resultSet.getInt("playcount"));
                track.setPublicationDate(resultSet.getString("publicationdate"));
                track.setDescription(resultSet.getString("description"));
                track.setOfflineAvailable(false);

                tracks.add(track);
            }

            return tracks;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
