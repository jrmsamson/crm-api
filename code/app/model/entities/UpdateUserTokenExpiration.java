package model.entities;

import java.time.LocalDateTime;

public class UpdateUserTokenExpiration {

    private Long userId;
    private LocalDateTime tokenExpiration;

    public UpdateUserTokenExpiration(Long userId, LocalDateTime tokenExpiration) {
        this.userId = userId;
        this.tokenExpiration = tokenExpiration;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(LocalDateTime tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }
}
