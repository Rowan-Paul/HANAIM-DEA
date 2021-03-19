package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Playlist;
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

//TODO: move certain track related functions to TrackDAO
//TODO: add offlineAvailable or something
@Default
public class PlaylistDAO implements IPlaylistDAO {
    @Resource(name = "jdbc/spotitube")
    DataSource dataSource;

    @Override
    public ArrayList<Playlist> getPlaylists() throws InternalServerError {
        String sql = "select * from playlists";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Playlist> playlists = new ArrayList<>();

            while (resultSet.next()) {
                Playlist playlist = new Playlist(resultSet.getString("name"),
                        resultSet.getString("owner"));
                playlist.setId(resultSet.getInt("id"));
                playlist.setTracks(getTracksFromPlaylist(resultSet.getInt("id")));
                playlist.setLength(calculatePlaylistLength(playlist.getTracks()));

                playlists.add(playlist);
            }

            return playlists;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public int calculatePlaylistLength(ArrayList<Track> tracks) {
        int length = 0;

        for (Track track : tracks) {
            length += track.getDuration();
        }

        return length;
    }

    @Override
    public ArrayList<Track> getTracksFromPlaylist(int playlistid) throws InternalServerError {
        String sql = "select * from playlisttracks where playlistid = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, playlistid);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Track> tracks = new ArrayList<>();

            while (resultSet.next()) {
                tracks.add(getTrackInfo(resultSet.getInt("trackid")));
            }

            return tracks;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public Track getTrackInfo(int trackid) throws InternalServerError {
        String sql = "select * from tracks where id = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, trackid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Track track = new Track(trackid,
                        resultSet.getString("title"),
                        resultSet.getString("performer"));
                track.setAlbum(resultSet.getString("album"));
                track.setDuration(resultSet.getInt("duration"));
                track.setPlaycount(resultSet.getInt("playcount"));
                track.setPublicationDate(resultSet.getString("publicationdate"));
                track.setDescription(resultSet.getString("description"));
                track.setOfflineAvailable(false);

                return track;
            }
            return null;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public boolean deletePlaylist(int playlistid) throws InternalServerError {
        String sql = "delete from playlists where id = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            int resultSet = statement.executeUpdate();

            if(deleteTracksInPlaylist(playlistid)){
                return true;
            }

            return false;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public boolean createPlaylist(Playlist playlist, String owner) throws InternalServerError {
        String sql = "insert into playlists (`name`, `owner`) values (?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlist.getName());
            statement.setString(2, owner);
            statement.executeUpdate();

            if(playlist.getTracks() != null) {
                int playlistId = getPlaylistIdFromName(playlist.getName());

                ArrayList<Track> tracks = playlist.getTracks();

                for (Track track : tracks) {
                    addTrackToPlaylist(playlistId, track.getId());
                }
            }

            return true;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public int getPlaylistIdFromName(String playlistname) throws InternalServerError {
        String sql = "select * from playlists where name = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlistname);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("id");
            }

            return 0;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public boolean addTrackToPlaylist(int playlistid, int trackid) throws InternalServerError {
        String sql = "insert into playlisttracks (`playlistid`, `trackid`) values (?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            statement.setInt(2, trackid);
            int resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public boolean editPlaylist(int playlistid, Playlist playlist) throws InternalServerError {
        String sql = "update playlists set name = ? where id = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlist.getName());
            statement.setInt(2, playlistid);
            int resultSet = statement.executeUpdate();

            if(playlist.getTracks().size() > 0) {
                deleteTracksInPlaylist(playlistid);
                ArrayList<Track> tracks = playlist.getTracks();

                for (Track track : tracks) {
                    addTrackToPlaylist(playlistid, track.getId());
                }
            }
            return true;
        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public boolean deleteTracksInPlaylist(int playlistid) throws InternalServerError {
        String sql = "delete from playlisttracks where playlistid = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            int resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    @Override
    public boolean deleteTrackInPlaylist(int playlistid, int trackid) throws InternalServerError {
        String sql = "delete from playlisttracks where playlistid = ? and trackid = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            statement.setInt(2, trackid);
            int resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            throw new InternalServerError(exception.toString());
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
