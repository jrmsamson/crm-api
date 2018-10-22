package model.entities;

import enums.Role;

import java.util.UUID;

public class EditUser {

    private final UUID uuid;
    private final String name;
    private final String surname;
    private final Role role;

    public EditUser(UUID uuid, String name, String surname, Role role) {
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Role getRole() {
        return role;
    }
}
