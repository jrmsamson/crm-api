package model.entities;

import java.util.UUID;

public class LoginResponse {

    private Long userId;
    private String password;
    private UUID passwordSalt;

    public LoginResponse(Long userId, String password, UUID passwordSalt) {
        this.userId = userId;
        this.password = password;
        this.passwordSalt = passwordSalt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(UUID passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
}
