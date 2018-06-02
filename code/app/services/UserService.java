package services;

import model.entities.UserRequest;
import model.entities.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<UUID> addUser(UserRequest userRequest);

    void editUser(UUID userUuid, UserRequest userRequest);

    void deleteUser(UUID uuid);

    List<UserResponse> getUsersActive();
}
