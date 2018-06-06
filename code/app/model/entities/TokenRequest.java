package model.entities;

public class TokenRequest {

    private Long userId;
    private Boolean isAdmin;

    public TokenRequest(Long userId, Boolean isAdmin) {
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
