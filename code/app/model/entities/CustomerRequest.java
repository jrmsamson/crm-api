package model.entities;

public class CustomerRequest {

    private String name;
    private String surname;
    private String photoName;

    public CustomerRequest() {
    }

    public CustomerRequest(String name, String surname, String photoName) {
        this.name = name;
        this.surname = surname;
        this.photoName = photoName;
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

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
