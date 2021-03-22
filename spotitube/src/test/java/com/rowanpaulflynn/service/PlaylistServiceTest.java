package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.PlaylistDTO;
import com.rowanpaulflynn.service.dto.PlaylistsDTO;
import com.rowanpaulflynn.service.dto.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlaylistServiceTest {
    private IPlaylistDAO playlistDAOMock = mock(IPlaylistDAO.class);
    private IUserDAO userDAOMock = mock(IUserDAO.class);
    private Token token = new Token("rowan");
    private User user = new User("rowan");
    private PlaylistService playlistService;
    private PlaylistService mockPlaylistService;

    ArrayList<Track> playlist1;
    ArrayList<Track> playlist2;
    ArrayList<Track> playlist3;
    PlaylistsDTO expectedPlaylistsDTO;
    ArrayList<Playlist> playlists;

    PlaylistDTO playlistDTO;
    Playlist playlist;

    @BeforeEach
    public void beforeEachSetUp() {
        playlistService = new PlaylistService();
        mockPlaylistService = Mockito.spy(playlistService);
        mockPlaylistService.setPlaylistDAO(playlistDAOMock);
        mockPlaylistService.setUserDAO(userDAOMock);

        // PlaylistDTO
        playlistDTO = new PlaylistDTO();
        playlistDTO.name = "playlistname";
        playlistDTO.owner = false;
        playlistDTO.id = -1;

        playlist = new Playlist(playlistDTO.name, user.getUser());
        playlist.setId(playlistDTO.id);
        playlist.setTracks(playlistDTO.tracks);
        playlist.setLength(playlistDTO.length);

        // PlaylistsDTO expectedPlaylistsDTO
        playlist1 = new ArrayList<>();
        playlist2 = new ArrayList<>();
        playlist3 = new ArrayList<>();
        expectedPlaylistsDTO = new PlaylistsDTO();

        Track tk1 = new Track(1, "the 1", "Taylor Swift");
        tk1.setAlbum("folklore");
        tk1.setDuration(200);
        tk1.setPlaycount(0);
        tk1.setPublicationDate("05-22-2020");
        tk1.setDescription("Some cool song");
        tk1.setOfflineAvailable(false);
        playlist1.add(tk1);
        playlist2.add(tk1);

        Track tk2 = new Track(2, "coney island", "Taylor Swift");
        tk2.setAlbum("evermore");
        tk2.setDuration(230);
        tk2.setPlaycount(0);
        tk2.setPublicationDate("05-22-2020");
        tk2.setDescription("Other cool song");
        tk2.setOfflineAvailable(false);
        playlist1.add(tk2);
        playlist3.add(tk2);

        expectedPlaylistsDTO.playlists = new ArrayList<>();
        expectedPlaylistsDTO.playlists.add(playlist1);
        expectedPlaylistsDTO.playlists.add(playlist2);
        expectedPlaylistsDTO.playlists.add(playlist3);

        // Playlists playlist
        playlists = new ArrayList<>();
        Playlist pl1 = new Playlist("new playlist",
                user.getUser());
        pl1.setId(1);
        pl1.setTracks(playlist1);
        pl1.setLength(460);
        playlists.add(pl1);

        Playlist pl2 = new Playlist("another",
                "notme");
        pl2.setId(1);
        pl2.setTracks(playlist1);
        pl2.setLength(460);
        playlists.add(pl2);
    }

    /**
     * getAllPlaylists()
     * */
    @Test
    public void getAllPlaylistsTest() {
        int expectedStatuscode = 200;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.getAllPlaylists(token.getToken());
        PlaylistsDTO responsePlaylistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(expectedStatuscode, response.getStatus());
        assertEquals(expectedPlaylistsDTO, responsePlaylistsDTO);
    }

    @Test
    public void getAllPlaylistsNoTokenTest() {
        int expectedStatuscode = 400;

        Response response = mockPlaylistService.getAllPlaylists(null);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void getAllPlaylistsNoUserTest() {
        int expectedStatuscode = 401;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.getAllPlaylists(token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    /**
     * getAllPlaylistsList()
     * */
    @Test
    public void getAllPlaylistsListTest() {
        when(playlistDAOMock.getPlaylists()).thenReturn(playlists);
        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);

        PlaylistsDTO playlistsDTO = mockPlaylistService.getAllPlaylistsList(user);

        assertEquals(playlistDAOMock.getPlaylists().size(), playlistsDTO.playlists.size());
    }

    /**
     * createPlaylist()
     * */
    @Test
    public void createPlaylistTest() {
        int expectedStatusCode = 200;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.createPlaylist(any(Playlist.class),any(String.class))).thenReturn(true);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.createPlaylist(playlistDTO,token.getToken());

        assertEquals(expectedStatusCode, response.getStatus());
    }

    @Test
    public void createPlaylistFailedTest() {
        int expectedStatuscode = 400;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.createPlaylist(playlist, user.getUser())).thenReturn(false);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.createPlaylist(playlistDTO, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void createPlaylistNoTokenTest() {
        int expectedStatuscode = 400;

        Response response = mockPlaylistService.createPlaylist(playlistDTO, null);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void createPlaylistNoUserTest() {
        int expectedStatuscode = 401;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.createPlaylist(playlistDTO, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    /**
     * editPlaylist()
     * */
    @Test
    public void editPlaylistTest() {
        int expectedStatuscode = 200;
        int playlistid = 1;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.editPlaylist(any(int.class), any(Playlist.class))).thenReturn(true);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.editPlaylistTracks(playlistid, playlistDTO, token.getToken());
        PlaylistsDTO responsePlaylistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void editPlaylistFailedTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.editPlaylist(playlistid, playlist)).thenReturn(false);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.editPlaylistTracks(playlistid, playlistDTO, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void editPlaylistNoTokenTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;

        Response response = mockPlaylistService.editPlaylistTracks(playlistid, playlistDTO, null);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void editPlaylistNoUserTest() {
        int expectedStatuscode = 401;
        int playlistid = 1;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.editPlaylistTracks(playlistid, playlistDTO, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    /**
     * deletePlaylist()
     * */
    @Test
    public void deletePlaylistTest() {
        int expectedStatuscode = 200;
        int playlistid = 1;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.deletePlaylist(playlistid)).thenReturn(true);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.deletePlaylist(playlistid, token.getToken());
        PlaylistsDTO responsePlaylistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(expectedStatuscode, response.getStatus());
        assertEquals(expectedPlaylistsDTO, responsePlaylistsDTO);
    }

    @Test
    public void deletePlaylistFailedTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.deletePlaylist(playlistid)).thenReturn(false);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.deletePlaylist(playlistid, token.getToken());
        PlaylistsDTO responsePlaylistsDTO = (PlaylistsDTO) response.getEntity();

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void deletePlaylistNoTokenTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;

        Response response = mockPlaylistService.deletePlaylist(playlistid, null);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void deletePlaylistNoUserTest() {
        int expectedStatuscode = 401;
        int playlistid = 1;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.deletePlaylist(playlistid, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    /**
     * deleteTrackInPlaylist()
     * */
    @Test
    public void deleteTrackInPlaylistTest() {
        int expectedStatuscode = 200;
        int playlistid = 1;
        int trackid = 2;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.deleteTrackInPlaylist(playlistid,trackid)).thenReturn(true);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.deleteTrackInPlaylist(playlistid, trackid, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void deleteTrackInPlaylistFailedTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;
        int trackid = 2;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.deleteTrackInPlaylist(playlistid,trackid)).thenReturn(false);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.deleteTrackInPlaylist(playlistid, trackid, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void deleteTrackInPlaylistNoTokenTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;
        int trackid = 2;

        Response response = mockPlaylistService.deleteTrackInPlaylist(playlistid, trackid, null);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void deleteTrackInPlaylistNoUserTest() {
        int expectedStatuscode = 401;
        int playlistid = 1;
        int trackid = 2;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);
        doReturn(expectedPlaylistsDTO).when(mockPlaylistService).getAllPlaylistsList(user);

        Response response = mockPlaylistService.deleteTrackInPlaylist(playlistid, trackid, token.getToken());

        assertEquals(expectedStatuscode, response.getStatus());
    }

    /**
     * addTrackToPlaylist
     * */
    @Test
    public void addTrackToPlaylistTest() {
        int expectedStatuscode = 201;
        int playlistid = 1;
        int trackid = 2;
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.id = trackid;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.addTrackToPlaylist(playlistid,trackid)).thenReturn(true);
        when(playlistDAOMock.getTracksFromPlaylist(playlistid)).thenReturn(playlist1);

        Response response = mockPlaylistService.addTrackToPlaylist(playlistid, token.getToken(), trackDTO);
        ArrayList<Track> responseTracks = (ArrayList<Track>) response.getEntity();

        assertEquals(expectedStatuscode, response.getStatus());
        assertEquals(playlist1.get(0).getTitle(), responseTracks.get(0).getTitle());
        assertEquals(playlist1.get(0).getAlbum(), responseTracks.get(0).getAlbum());
        assertEquals(playlist1.get(0).getPlaycount(), responseTracks.get(0).getPlaycount());
        assertEquals(playlist1.get(0).getDescription(), responseTracks.get(0).getDescription());
        assertEquals(playlist1.get(0).getOfflineAvailable(), responseTracks.get(0).getOfflineAvailable());
        assertEquals(playlist1.get(0).getPublicationDate(), responseTracks.get(0).getPublicationDate());
    }

    @Test
    public void addTrackToPlaylistFailedTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;
        int trackid = 2;
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.id = trackid;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.addTrackToPlaylist(playlistid,trackid)).thenReturn(false);

        Response response = mockPlaylistService.addTrackToPlaylist(playlistid, token.getToken(), trackDTO);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void addTrackToPlaylistNoTokenTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;
        int trackid = 2;
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.id = trackid;

        Response response = mockPlaylistService.addTrackToPlaylist(playlistid, null, trackDTO);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void addTrackToPlaylistNoUserTest() {
        int expectedStatuscode = 401;
        int playlistid = 1;
        int trackid = 2;
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.id = trackid;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);

        Response response = mockPlaylistService.addTrackToPlaylist(playlistid, token.getToken(), trackDTO);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    /**
     * getTracks
     */
    @Test
    public void getTracksTest() {
        int expectedStatuscode = 200;
        int playlistid = 1;
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.id = 1;
        trackDTO.title = "the 1";
        trackDTO.performer = "Taylor Swift";

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.getTracksFromPlaylist(playlistid)).thenReturn(playlist1);

        Response response = mockPlaylistService.getTracks(token.getToken(), playlistid);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void getTracksFailedTest() {
        int expectedStatuscode = 404;
        int playlistid = 1;
        ArrayList<Track> emptyPlaylist = new ArrayList<>();

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);
        when(playlistDAOMock.getTracksFromPlaylist(playlistid)).thenReturn(emptyPlaylist);

        Response response = mockPlaylistService.getTracks(token.getToken(), playlistid);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void getTracksNoTokenTest() {
        int expectedStatuscode = 400;
        int playlistid = 1;

        Response response = mockPlaylistService.getTracks(null, playlistid);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void getTracksNoUserTest() {
        int expectedStatuscode = 401;
        int playlistid = 1;

        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);

        Response response = mockPlaylistService.getTracks(token.getToken(), playlistid);

        assertEquals(expectedStatuscode, response.getStatus());
    }
}
