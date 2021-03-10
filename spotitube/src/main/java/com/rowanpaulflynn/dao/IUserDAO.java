package com.rowanpaulflynn.dao;

import com.rowanpaulflynn.domain.Token;
import com.rowanpaulflynn.domain.User;

public interface IUserDAO {
    User getUser(String user);

    Token createToken(String user);
}