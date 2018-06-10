package services.impl;

import enums.Role;
import exceptions.RoleDoesNotExistException;
import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import model.entities.*;
import model.pojos.User;
import repositories.RepositoryFactory;
import services.UserService;
import util.CryptoUtils;
import util.Notification;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

    private static final Integer TOKEN_EXPIRATION_DURATION_MINUTES = 30;


    @Inject
    public UserServiceImpl(RepositoryFactory repositoryFactory) {
        // For testing purpose
        super(repositoryFactory);
    }

    public Long addUser(UserRequest userRequest) {
        Notification userRequestNotification = userRequest.validation();
        if (userRequestNotification.hasErrors())
            throw new UserRequestException(userRequestNotification.errorMessage());

        return repositoryFactory.getUserRepository().addUser(buildUser(userRequest));
    }

    private Integer getUserRoleId(Role role) {
        return repositoryFactory
                .getRoleRepository()
                .getRoleId(role)
                .orElseThrow(RoleDoesNotExistException::new);
    }

    public void editUser(UUID userUuid, UserRequest userRequest) {
        User user = buildUser(userRequest);
        user.setUuid(userUuid);
        repositoryFactory
                .getUserRepository()
                .editUser(user);
    }

    private User buildUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setRoleId(getUserRoleId(userRequest.getRole()));
        return user;
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

    public Role getUserRole(Long userId) {
        return repositoryFactory
                .getUserRepository()
                .getUserRoleByUserId(userId)
                .orElseThrow(UserDoesNotExistException::new);
    }

    @Override
    public UserTokenResponse getUserToken(Long userId) {
        return repositoryFactory
                .getUserRepository()
                .getUserToken(userId)
                .orElseThrow(UserDoesNotExistException::new);
    }

    @Override
    public void removeCurrentUserToken() {
        repositoryFactory
                .getUserRepository()
                .removeUserToken(currentUserId);
    }

    public void updateUserTokenExpiration(Long userId) {
        repositoryFactory
                .getUserRepository()
                .updateUserTokenExpiration(new UpdateUserTokenExpiration(
                        userId,
                        getTokenExpiration()
                ));
    }

    public String buildUserToken(Long userId) {
        String token = CryptoUtils.generateSecureRandomToken();
        repositoryFactory.getUserRepository().updateUserToken(
                new UpdateUserTokenRequest(
                        userId,
                        token,
                        getTokenExpiration()
                )
        );

        return token;
    }

    private LocalDateTime getTokenExpiration() {
        return LocalDateTime
                .now()
                .plusMinutes(TOKEN_EXPIRATION_DURATION_MINUTES);
    }


}
