package model.entities;

import util.Notification;

public class UserRequest {

    public static final String NAME_REQUIRED_MESSAGE = "Name is required";
    public static final String SURNAME_REQUIRED_MESSAGE = "Surname is required";

    private String name;
    private String surname;

    public UserRequest(String name, String surname) {
        this.name = name;
        this.surname = surname;
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
}
