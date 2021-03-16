package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.PlaylistDTO;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Default
public class PlaylistDAO implements IPlaylistDAO {
    @Resource(name="jdbc/spotitube")
    DataSource dataSource;

    //TODO: implement length of playlist
    @Override
    public ArrayList<Playlist> getPlaylists() {
        String sql = "select * from playlists";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Playlist> playlists = new ArrayList<>();

            while (resultSet.next()){
                Playlist playlist = new Playlist(resultSet.getString("name"),
                        resultSet.getString("owner"));
                playlist.setId(resultSet.getInt("id"));
                playlist.setTracks(getTracksFromPlaylist(resultSet.getInt("id")));

                playlists.add(playlist);
            }

            return playlists;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Track> getTracksFromPlaylist(int playlistid) {
        String sql = "select * from playlisttracks where playlistid = ?";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,playlistid);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Track> tracks = new ArrayList<>();

            while (resultSet.next()){
                tracks.add(getTrackInfo(resultSet.getInt("track")));
            }

            return tracks;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


    @Override
    public Track getTrackInfo(int trackid) {
        String sql = "select * from tracks where id = ?";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,trackid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Track track = new Track(trackid,
                        resultSet.getString("title"),
                        resultSet.getString("performer"));

                return track;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean deletePlaylist(int playlistid) {
        //TODO: also remove playlist from playlisttracks
        String sql = "delete from playlists where id = ?";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,playlistid);
            int resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    //TODO: add length to playlists
    @Override
    public Boolean createPlaylist(PlaylistDTO playlistDTO, String owner) {
        String sql = "insert into playlists (`name`, `owner`) values (?, ?)";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playlistDTO.name);
            statement.setString(2,owner);
            int resultSet = statement.executeUpdate();

            int playlistId = getPlaylistIdFromName(playlistDTO.name);

            for (Object trackDTO : playlistDTO.tracks) {
                HashMap tk = (HashMap) trackDTO;
                Number trackid = (Number) tk.get("id");

                addTrackToPlaylist(playlistId, trackid.intValue());
            }

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public int getPlaylistIdFromName(String playlistname) {
        String sql = "select id from playlists where name = ?";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,playlistname);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("id");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return 0;
    }

    @Override
    public Boolean addTrackToPlaylist(int playlistid, int trackid) {
        String sql = "insert into playlisttracks (`playlistid`, `trackid`) values (?, ?)";

        try(Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,playlistid);
            statement.setInt(2,trackid);
            int resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
