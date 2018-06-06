package services.impl;

public class AddLoginData {

    private final String username;
    private final String password;
    private final Long userId;

    public AddLoginData(String username, String password, Long userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getUserId() {
        return userId;
    }
}
