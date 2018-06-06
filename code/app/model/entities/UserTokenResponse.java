package model.entities;

import java.time.LocalDateTime;

public class UserTokenResponse {

    private String token;
    private LocalDateTime tokenExpiration;

    public UserTokenResponse(String token, LocalDateTime tokenExpiration) {
        this.token = token;
        this.tokenExpiration = tokenExpiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(LocalDateTime tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }
}
