package repositories;

import model.entities.UserData;
import model.entities.UserResponse;
import model.pojos.User;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<UUID> addUser(DSLContext dslContext, User user);

    void editUser(DSLContext dslContext, UUID userUuid, UserData user);

    void deleteUser(DSLContext dslContext, UUID userUuid);

    Optional<UserResponse> getUser(DSLContext dslContext, UUID userUuid);

    List<UserResponse> getUsersActive(DSLContext dslContext);
}
