package model.entities.responses;

import model.pojos.User;

import java.util.UUID;

public class UserResponse {

    private final String name;
    private final String surname;
    private final UUID uuid;
    private final String role;

    public UserResponse(User user, String role) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.uuid = user.getUuid();
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getRole() {
        return role;
    }
}
