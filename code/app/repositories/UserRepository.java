package repositories;

import enums.Role;
import model.entities.EditUser;
import model.entities.AddUser;
import model.entities.NewToken;
import model.entities.responses.UserResponse;
import model.entities.responses.UserTokenResponse;
import model.pojos.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository {

    UUID addUser(AddUser user);

    void editUser(EditUser user);

    void deleteUserByUuid(UUID userUuid);

    Optional<UserResponse> getUserByUuid(UUID userUuid);

    List<UserResponse> getUsersActive(Long currentUserId);

    Optional<Role> getUserRoleByUserId(Long userId);

    Optional<UserTokenResponse> getUserTokenByUserId(Long userId);

    void updateUserToken(Long userId, NewToken token);

    void removeUserToken(Long currentUserId);

    void updateUserTokenExpirationByUserId(Long userId, LocalDateTime tokenExpiration);

    Optional<Long> getUserIdByUuid(UUID uuid);

    Optional<UserResponse> getUserByNameAndSurname(String name, String surname);
}
