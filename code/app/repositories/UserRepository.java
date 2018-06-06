package repositories;

import enums.Role;
import model.entities.AddUserRequest;
import model.entities.UpdateUserTokenRequest;
import model.entities.UserResponse;
import model.entities.UserTokenResponse;
import model.pojos.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository {

    Long addUser(User user);

    void editUser(UUID userUuid, AddUserRequest user);

    void deleteUser(UUID userUuid);

    Optional<UserResponse> getUserById(Long userId);

    Optional<UserResponse> getUserByUuid(UUID userUuid);

    List<UserResponse> getUsersActive();

    Optional<Role> getUserRoleByUserId(Long userId);

    Optional<UserTokenResponse> getUserToken(Long userId);

    void updateUserToken(UpdateUserTokenRequest updateUserTokenRequest);
}
