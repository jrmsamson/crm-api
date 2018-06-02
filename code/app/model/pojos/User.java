/*
 * This file is generated by jOOQ.
*/
package model.pojos;


import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class User implements Serializable {

    private static final long serialVersionUID = 1305954633;

    private Long    id;
    private UUID    uuid;
    private String  name;
    private String  surname;
    private Boolean active;

    public User() {}

    public User(User value) {
        this.id = value.id;
        this.uuid = value.uuid;
        this.name = value.name;
        this.surname = value.surname;
        this.active = value.active;
    }

    public User(
        Long    id,
        UUID    uuid,
        String  name,
        String  surname,
        Boolean active
    ) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.active = active;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    @Size(max = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Size(max = 50)
    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User (");

        sb.append(id);
        sb.append(", ").append(uuid);
        sb.append(", ").append(name);
        sb.append(", ").append(surname);
        sb.append(", ").append(active);

        sb.append(")");
        return sb.toString();
    }
}