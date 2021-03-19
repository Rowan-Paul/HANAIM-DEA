package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.IUserDAO;
import com.rowanpaulflynn.domain.Playlist;
import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.Track;
import com.rowanpaulflynn.domain.User;
import com.rowanpaulflynn.service.dto.PlaylistsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlaylistServiceTest {
    IPlaylistDAO playlistDAOMock = mock(IPlaylistDAO.class);
    IUserDAO userDAOMock = mock(IUserDAO.class);
    Token token = new Token("rowan");
    User user = new User("rowan");
    ArrayList<Track> playlist1;
    ArrayList<Track> playlist2;
    ArrayList<Track> playlist3;
    PlaylistsDTO expectedPlaylistsDTO;
    ArrayList<Playlist> playlists;
    private PlaylistService playlistService;
    private PlaylistService mockPlaylistService;

    @BeforeEach
    public void beforeEachSetUp() {
        playlistService = new PlaylistService();
        mockPlaylistService = Mockito.spy(playlistService);
        mockPlaylistService.setPlaylistDAO(playlistDAOMock);
        mockPlaylistService.setUserDAO(userDAOMock);

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

    @Test
    public void getAllPlaylistsListTest() {
        when(playlistDAOMock.getPlaylists()).thenReturn(playlists);
        when(userDAOMock.verifyToken(token.getToken())).thenReturn(user);

        PlaylistsDTO playlistsDTO = mockPlaylistService.getAllPlaylistsList(user);

        assertEquals(playlistDAOMock.getPlaylists().size(), playlistsDTO.playlists.size());
    }
}

