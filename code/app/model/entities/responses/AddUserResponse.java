package model.entities.responses;

import java.util.UUID;

public class AddUserResponse {

    private UUID userUuid;

    public AddUserResponse() {
    }

    public AddUserResponse(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }
}
