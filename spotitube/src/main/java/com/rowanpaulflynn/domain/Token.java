package com.rowanpaulflynn.domain;

import java.util.UUID;

public class Token {
    private String token;
    private String user;

    public Token(String user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
    }

    public String getToken() {
        return token;
    }

    public String getUser() {
        return user;
    }
}
