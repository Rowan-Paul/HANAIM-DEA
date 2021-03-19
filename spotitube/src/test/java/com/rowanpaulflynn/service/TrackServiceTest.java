package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.ITrackDAO;
import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrackServiceTest {
    private TrackService trackService;
    private PlaylistService playlistService;
    private UserService userService;
    ITrackDAO trackDAOMock = mock(ITrackDAO.class);
    IUserDAO userDAOMock = mock(IUserDAO.class);
    IPlaylistDAO playlistDAOMock = mock(IPlaylistDAO.class);
    Token token = new Token("rowan");
    User user = new User("rowan");

    ArrayList<Track> playlist1 = new ArrayList<>();
    ArrayList<Track> playlist2 = new ArrayList<>();
    
    public void setup() {
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
        playlist1.add(tk1);
    }
    
    @BeforeEach
    public void beforeEachSetUp() {
        trackService = new TrackService();
        playlistService = new PlaylistService();
        userService = new UserService();
    }

    @Test
    public void getTracksTest() {
        int expectedStatuscode = 200;
        int expectedPlaylistId = 1;

        doReturn(playlist2).when(playlistDAOMock).getTracksFromPlaylist(expectedPlaylistId);
        doReturn(playlist1).when(trackDAOMock).getTracks();
        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);

        trackService.setTrackDAO(trackDAOMock);
        trackService.setUserDAO(userDAOMock);
        trackService.setPlaylistDAO(playlistDAOMock);

        Response response = trackService.getTracks(token.getToken(), expectedPlaylistId);

        assertEquals(expectedStatuscode, response.getStatus());
        assertEquals(playlist2, response.getEntity());
    }
}