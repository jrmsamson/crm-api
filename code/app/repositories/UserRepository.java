package repositories;

import enums.Role;
import model.entities.*;
import model.pojos.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository {

    UUID addUser(User user);

    void editUser(User user);

    void deleteUser(UUID userUuid);

    Optional<UserResponse> getUserByUuid(UUID userUuid);

    List<UserResponse> getUsersActive();

    Optional<Role> getUserRoleByUserId(Long userId);

    Optional<UserTokenResponse> getUserToken(Long userId);

    void updateUserToken(UpdateUserTokenRequest updateUserTokenRequest);

    void removeUserToken(Long currentUserId);

    void updateUserTokenExpiration(UpdateUserTokenExpiration updateUserTokenExpiration);

    Optional<Long> getUserIdByUuid(UUID uuid);
}
