package model.entities;

import enums.Role;

import java.util.UUID;

public class UserResponse {

    private String name;
    private String surname;
    private UUID uuid;
    private Role role;

    public UserResponse(String name, String surname, UUID uuid, Role role) {
        this.name = name;
        this.surname = surname;
        this.uuid = uuid;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
