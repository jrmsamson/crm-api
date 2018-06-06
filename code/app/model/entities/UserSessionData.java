package model.entities;

import enums.Role;

public class UserSessionData {

    private Long userId;
    private Role role;

    public UserSessionData(Long userId, Role role) {
        this.userId = userId;
        this.role = role;
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
}
