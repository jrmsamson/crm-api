package services;

import enums.Role;
import model.entities.AddUserRequest;
import model.entities.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService extends BaseService {

    Long addUser(AddUserRequest addUserRequest);

    void editUser(UUID userUuid, AddUserRequest addUserRequest);

    void deleteUser(UUID uuid);

    List<UserResponse> getUsersActive();

    Role getUserRole(Long userId);
}
