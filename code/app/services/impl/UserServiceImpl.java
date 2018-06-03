package services.impl;

import exceptions.UserRequestException;
import model.entities.UserRequest;
import model.entities.UserResponse;
import repositories.RepositoryFactory;
import services.UserService;
import util.Notification;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Inject
    public UserServiceImpl(RepositoryFactory repositoryFactory) {
        // For testing purpose
        super(repositoryFactory);
    }

    public Optional<UUID> addUser(UserRequest userRequest) {
        Notification userRequestNotification = userRequest.validation();
        if (userRequestNotification.hasErrors())
            throw new UserRequestException(userRequestNotification.errorMessage());
        return repositoryFactory.getUserRepository().addUser(userRequest);
    }

    public void editUser(UUID userUuid, UserRequest userRequest) {
        repositoryFactory
                .getUserRepository()
                .editUser(userUuid, userRequest);
    }

    public void deleteUser(UUID userUuid) {
        repositoryFactory
                .getUserRepository()
                .deleteUser(userUuid);
    }

    public List<UserResponse> getUsersActive() {
        return repositoryFactory
                .getUserRepository()
                .getUsersActive();
    }
}
