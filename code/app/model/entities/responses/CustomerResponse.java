package model.entities.responses;

import model.pojos.Customer;

import java.util.UUID;

public class CustomerResponse {

    private UUID uuid;
    private String name;
    private String surname;
    private String photoUrl;

    public CustomerResponse(UUID uuid, String name, String surname, String photoUrl) {
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.photoUrl = photoUrl;
    }

    public CustomerResponse(Customer customer) {
        this.uuid = customer.getUuid();
        this.name = customer.getName();
        this.surname = customer.getSurname();
        this.photoUrl = customer.getPhotoName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
