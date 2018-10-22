package model.entities;

import java.time.LocalDateTime;

public class NewToken {

    private final String token;
    private final LocalDateTime tokenExpiration;

    public NewToken(String token, LocalDateTime tokenExpiration) {
        this.token = token;
        this.tokenExpiration = tokenExpiration;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }
}
