package services.impl;

import model.entities.UserRequest;
import model.entities.UserResponse;
import repositories.UserRepository;
import services.UserService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UUID> addUser(UserRequest userRequest) {
        return userRepository.addUser(userRequest);
    }

    @Override
    public void editUser(UUID userUuid, UserRequest userRequest) {
        userRepository.editUser(userUuid, userRequest);
    }

    @Override
    public void deleteUser(UUID userUuid) {
        userRepository.deleteUser(userUuid);
    }

    @Override
    public List<UserResponse> getUsersActive() {
        return userRepository.getUsersActive();
    }
}
