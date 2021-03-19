package com.rowanpaulflynn.service;

import com.rowanpaulflynn.dao.IPlaylistDAO;
import com.rowanpaulflynn.dao.IUserDAO;
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
    private PlaylistService playlistService;
    private PlaylistService mockPlaylistService;
    IPlaylistDAO playlistDAOMock = mock(IPlaylistDAO.class);
    IUserDAO userDAOMock = mock(IUserDAO.class);
    Token token = new Token("rowan");
    User user = new User("rowan");

    ArrayList<Track> playlist1;
    ArrayList<Track> playlist2;
    ArrayList<Track> playlist3;
    PlaylistsDTO expectedPlaylistsDTO;

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

        expectedPlaylistsDTO.playlists = new ArrayList<>();
        expectedPlaylistsDTO.playlists.add(playlist1);
        expectedPlaylistsDTO.playlists.add(playlist2);
        expectedPlaylistsDTO.playlists.add(playlist3);
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
}
