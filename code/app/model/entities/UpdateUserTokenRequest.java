package model.entities;

import java.time.LocalDateTime;

public class UpdateUserTokenRequest {

    private Long userId;
    private String token;
    private LocalDateTime tokenExpiration;

    public UpdateUserTokenRequest(Long userId, String token, LocalDateTime tokenExpiration) {
        this.userId = userId;
        this.token = token;
        this.tokenExpiration = tokenExpiration;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
