package model.entities;

import play.Logger;
import util.Notification;

import java.time.LocalDateTime;

public class UserTokenResponse {

    private String token;
    private LocalDateTime tokenExpiration;

    public UserTokenResponse(String token, LocalDateTime tokenExpiration) {
        this.token = token;
        this.tokenExpiration = tokenExpiration;
    }

    public Boolean validate(String token) {
        return token != null
                || !token.isEmpty()
                || token.equals(this.token)
                || tokenExpiration.isAfter(LocalDateTime.now());
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
