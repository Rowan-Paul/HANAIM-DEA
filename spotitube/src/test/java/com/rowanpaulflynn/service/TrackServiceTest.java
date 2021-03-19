package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.ITrackDAO;
import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.TracklistDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrackServiceTest {
    private TrackService trackService;
    ITrackDAO trackDAOMock = mock(ITrackDAO.class);
    IUserDAO userDAOMock = mock(IUserDAO.class);
    IPlaylistDAO playlistDAOMock = mock(IPlaylistDAO.class);
    Token token = new Token("rowan");
    User user = new User("rowan");

    ArrayList<Track> playlist1 = new ArrayList<>();
    ArrayList<Track> playlist2 = new ArrayList<>();
    ArrayList<Track> playlist3 = new ArrayList<>();
    
    @BeforeEach
    public void beforeEachSetUp() {
        trackService = new TrackService();
        trackService.setTrackDAO(trackDAOMock);
        trackService.setUserDAO(userDAOMock);
        trackService.setPlaylistDAO(playlistDAOMock);

        Track tk1 = new Track(1,"the 1","Taylor Swift");
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
    }

    /**
     * getTracks()
     * */
    @Test
    public void getTracksTest() {
        int expectedStatuscode = 200;
        int expectedPlaylistId = 1;
        TracklistDTO expectedTracklist = new TracklistDTO();
        expectedTracklist.tracks = playlist3;

        doReturn(playlist2).when(playlistDAOMock).getTracksFromPlaylist(expectedPlaylistId);
        doReturn(playlist1).when(trackDAOMock).getTracks();
        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);

        Response response = trackService.getTracks(token.getToken(), expectedPlaylistId);
        TracklistDTO responseTracklist = (TracklistDTO) response.getEntity();

        assertEquals(expectedStatuscode, response.getStatus());
        assertEquals(expectedTracklist.tracks.size(), responseTracklist.tracks.size());
    }

    @Test
    public void getTracksAlternativeTest() {
        int expectedStatuscode = 200;
        int expectedPlaylistId = 1;
        TracklistDTO expectedTracklist = new TracklistDTO();
        expectedTracklist.tracks = playlist3;
        ArrayList<Track> inPlaylist = new ArrayList<>();

        doReturn(inPlaylist).when(playlistDAOMock).getTracksFromPlaylist(expectedPlaylistId);
        doReturn(playlist1).when(trackDAOMock).getTracks();
        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);

        Response response = trackService.getTracks(token.getToken(), expectedPlaylistId);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void getTracksTokenNullTest() {
        int expectedStatuscode = 400;
        int expectedPlaylistId = 1;

        doReturn(playlist2).when(playlistDAOMock).getTracksFromPlaylist(expectedPlaylistId);
        doReturn(playlist1).when(trackDAOMock).getTracks();
        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);

        Response response = trackService.getTracks(null, expectedPlaylistId);

        assertEquals(expectedStatuscode, response.getStatus());
    }

    @Test
    public void getTracksNoUserTest() {
        int expectedStatuscode = 401;
        int expectedPlaylistId = 1;

        doReturn(playlist2).when(playlistDAOMock).getTracksFromPlaylist(expectedPlaylistId);
        doReturn(playlist1).when(trackDAOMock).getTracks();
        when(userDAOMock.verifyToken(token.getToken())).thenReturn(null);

        Response response = trackService.getTracks(token.getToken(), expectedPlaylistId);

        assertEquals(expectedStatuscode, response.getStatus());
    }
}