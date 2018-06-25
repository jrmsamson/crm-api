package model.entities.requests;

import com.google.common.base.Strings;
import util.Notification;

public class UserRequest {

    public static final String NAME_REQUIRED_MESSAGE = "Name is required";
    public static final String SURNAME_REQUIRED_MESSAGE = "Surname is required";
    public static final String ROLE_REQUIRED_MESSAGE = "Role is required";
    public static final String USERNAME_REQUIRED_MESSAGE = "Username is required";
    public static final String PASSWORD_REQUIRED_MESSAGE = "Password is required";

    private String name;
    private String surname;
    private String role;
    private String username;
    private String password;

    public UserRequest() {
    }

    public UserRequest(String name, String surname, String role, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public Notification validation() {
        Notification notification = new Notification();

        if (Strings.isNullOrEmpty(name))
            notification.addError(NAME_REQUIRED_MESSAGE);

        if (Strings.isNullOrEmpty(surname))
            notification.addError(SURNAME_REQUIRED_MESSAGE);

        if (Strings.isNullOrEmpty(role))
            notification.addError(ROLE_REQUIRED_MESSAGE);

        if (Strings.isNullOrEmpty(username))
            notification.addError(USERNAME_REQUIRED_MESSAGE);

        if (Strings.isNullOrEmpty(password))
            notification.addError(PASSWORD_REQUIRED_MESSAGE);

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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
