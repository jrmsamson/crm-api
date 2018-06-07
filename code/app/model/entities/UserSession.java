package model.entities;

import enums.Role;

public class UserSession {

    private Long userId;
    private Role role;
    private String token;

    public UserSession(Long userId, Role role, String token) {
        this.userId = userId;
        this.role = role;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
