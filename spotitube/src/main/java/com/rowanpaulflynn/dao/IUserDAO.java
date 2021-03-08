package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.User;

public interface IUserDAO {
    User getUser(String id);
}