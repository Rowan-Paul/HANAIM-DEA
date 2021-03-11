package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Track;

import java.util.ArrayList;

public interface ITrackDAO {
    ArrayList<Track> getTracks();
}