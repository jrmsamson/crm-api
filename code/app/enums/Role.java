package enums;

import exceptions.RoleDoesNotExistException;

public enum Role {
    ADMIN("Admin"),
    USER("User");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Role lookup(String name) {
        try {
            return Role.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RoleDoesNotExistException();
        }
    }


}


