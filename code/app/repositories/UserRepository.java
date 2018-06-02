package repositories;

import model.entities.UserRequest;
import model.entities.UserResponse;
import model.pojos.User;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<UUID> addUser(User user);

    void editUser(UUID userUuid, UserRequest user);

    void deleteUser(UUID userUuid);

    Optional<UserResponse> getUser(UUID userUuid);

    List<UserResponse> getUsersActive();
}
