package model.entities;

import enums.Role;
import util.Notification;

public class AddUserRequest {

    public static final String NAME_REQUIRED_MESSAGE = "Name is required";
    public static final String SURNAME_REQUIRED_MESSAGE = "Surname is required";

    private String name;
    private String surname;
    private Role role;
    private String username;
    private String password;

    public AddUserRequest() {
    }

    public AddUserRequest(String name, String surname, Role role, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public Notification validation() {
        Notification notification = new Notification();

        if (name.isEmpty())
            notification.addError(NAME_REQUIRED_MESSAGE);

        if (surname.isEmpty())
            notification.addError(SURNAME_REQUIRED_MESSAGE);

        return notification;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
}
