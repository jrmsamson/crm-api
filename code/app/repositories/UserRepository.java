package repositories;

import enums.Role;
import model.entities.requests.UpdateUserTokenExpirationRequest;
import model.entities.requests.UpdateUserTokenRequest;
import model.entities.responses.UserResponse;
import model.entities.responses.UserTokenResponse;
import model.pojos.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository {

    UUID addUser(User user);

    void editUserByUuid(User user);

    void deleteUserByUuid(UUID userUuid);

    Optional<UserResponse> getUserByUuid(UUID userUuid);

    List<UserResponse> getUsersActive(Long currentUserId);

    Optional<Role> getUserRoleByUserId(Long userId);

    Optional<UserTokenResponse> getUserTokenByUserId(Long userId);

    void updateUserTokenByUserId(User user);

    void removeUserToken(Long currentUserId);

    void updateUserTokenExpirationByUserId(User user);

    Optional<Long> getUserIdByUuid(UUID uuid);

    Boolean existAndUserWithTheSameNameAndSurname(User user);
}
