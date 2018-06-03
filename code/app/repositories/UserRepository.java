package repositories;

import model.entities.UserRequest;
import model.entities.UserResponse;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void setDslContext(DSLContext dslContext);

    Optional<UUID> addUser(UserRequest user);

    void editUser(UUID userUuid, UserRequest user);

    void deleteUser(UUID userUuid);

    Optional<UserResponse> getUser(UUID userUuid);

    List<UserResponse> getUsersActive();
}
