package model.entities.requests;

import com.google.common.base.Strings;
import util.Notification;

public class CustomerRequest {

    public static final String NAME_REQUIRED_MESSAGE = "Name is required";
    public static final String SURNAME_REQUIRED_MESSAGE = "Surname is required";

    private String name;
    private String surname;

    public CustomerRequest() {
    }

    public CustomerRequest(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Notification validation() {
        Notification notification = new Notification();

        if (Strings.isNullOrEmpty(name))
            notification.addError(NAME_REQUIRED_MESSAGE);

        if (Strings.isNullOrEmpty(surname))
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
