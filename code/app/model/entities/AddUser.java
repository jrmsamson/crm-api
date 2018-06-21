package model.entities;

import enums.Role;

public class AddUser {

    private final String name;
    private final String surname;
    private final Role role;

    public AddUser(String name, String surname, Role role) {
        this.name = name;
        this.surname = surname;
        this.role = role;
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
