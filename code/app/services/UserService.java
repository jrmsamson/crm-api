package services;

import enums.Role;
import model.entities.responses.AddUserResponse;
import model.entities.requests.UserRequest;
import model.entities.responses.UserResponse;
import model.entities.responses.UserToken;

import java.util.List;
import java.util.UUID;

public interface UserService extends BaseService {

    AddUserResponse addUser(UserRequest userRequest);

    void updateUser(UUID userUuid, UserRequest userRequest);

    void deleteUser(UUID uuid);

    List<UserResponse> getUsersActive();

    UserToken getUserTokenByUserId(Long userId);

    void removeCurrentUserToken();

    String setUserTokenByUserId(Long userId);

    void renewUserToken(Long userId);

    Long getUserIdByUuid(UUID userUuid);

    UserResponse getUserByUuid(UUID userUuid);

    String getUserRoleByUserId(Long userId);
}
