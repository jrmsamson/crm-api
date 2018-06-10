package services;

import enums.Role;
import model.entities.AddUserResponse;
import model.entities.UserRequest;
import model.entities.UserResponse;
import model.entities.UserTokenResponse;

import java.util.List;
import java.util.UUID;

public interface UserService extends BaseService {

    AddUserResponse addUser(UserRequest userRequest);

    void editUser(UUID userUuid, UserRequest userRequest);

    void deleteUser(UUID uuid);

    List<UserResponse> getUsersActive();

    Role getUserRole(Long userId);

    UserTokenResponse getUserToken(Long userId);

    void removeCurrentUserToken();

    String buildUserToken(Long userId);

    void updateUserTokenExpiration(Long userId);

    Long getUserIdByUuid(UUID userUuid);
}
