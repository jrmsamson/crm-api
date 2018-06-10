package model.entities;

public class AddEditLogin {

    private String username;
    private String password;
    private Long userId;

    public AddEditLogin(String username, String password, Long userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
