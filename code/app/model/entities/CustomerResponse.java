package model.entities;

import java.util.UUID;

public class CustomerResponse {

    private String name;
    private String surname;
    private UUID uuid;

    public CustomerResponse(String name, String surname, UUID uuid) {
        this.name = name;
        this.surname = surname;
        this.uuid = uuid;
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
}
