package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.service.dto.PlaylistDTO;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

//TODO: move certain track related functions to TrackDAO
//TODO: don't use DTO here
@Default
public class PlaylistDAO implements IPlaylistDAO {
    @Resource(name = "jdbc/spotitube")
    DataSource dataSource;

    @Override
    public ArrayList<Playlist> getPlaylists() {
        String sql = "select * from playlists";

        try (Connection connection = dataSource.getConnection();) {
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
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Track> getTracksFromPlaylist(int playlistid) {
        String sql = "select * from playlisttracks where playlistid = ?";

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<Track> tracks = new ArrayList<>();

            while (resultSet.next()) {
                tracks.add(getTrackInfo(resultSet.getInt("trackid")));
            }

            return tracks;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int calculatePlaylistLength(ArrayList<Track> tracks) {
        int length = 0;

        for (Track track : tracks) {
            length += track.getDuration();
        }

        return length;
    }


    @Override
    public Track getTrackInfo(int trackid) {
        String sql = "select * from tracks where id = ?";

        try (Connection connection = dataSource.getConnection();) {
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
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean deletePlaylist(int playlistid) {
        String sql = "delete from playlists where id = ?";

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            int resultSet = statement.executeUpdate();

            deleteTracksInPLaylist(playlistid);

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public Boolean createPlaylist(PlaylistDTO playlistDTO, String owner) {
        String sql = "insert into playlists (`name`, `owner`) values (?, ?)";

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlistDTO.name);
            statement.setString(2, owner);
            int resultSet = statement.executeUpdate();

            if (playlistDTO.tracks != null) {
                int playlistId = getPlaylistIdFromName(playlistDTO.name);

                for (Object trackDTO : playlistDTO.tracks) {
                    HashMap tk = (HashMap) trackDTO;
                    Number trackid = (Number) tk.get("id");

                    addTrackToPlaylist(playlistId, trackid.intValue());
                }
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

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlistname);
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

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            statement.setInt(2, trackid);
            int resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public Boolean editPlaylist(int playlistid, PlaylistDTO playlistDTO) {
        String sql = "update playlists set name = ? where id = ?";

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlistDTO.name);
            statement.setInt(2, playlistid);
            int resultSet = statement.executeUpdate();

            deleteTracksInPLaylist(playlistid);
            for (Object track : playlistDTO.tracks) {
                HashMap tk = (HashMap) track;
                Number trackid = (Number) tk.get("id");

                addTrackToPlaylist(playlistid, trackid.intValue());
            }

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public Boolean deleteTracksInPLaylist(int playlistid) {
        String sql = "delete from playlisttracks where playlistid = ?";

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            int resultSet = statement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public Boolean deleteTrackInPLaylist(int playlistid, int trackid) {
        String sql = "delete from playlisttracks where playlistid = ? and trackid = ?";

        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistid);
            statement.setInt(2, trackid);
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
