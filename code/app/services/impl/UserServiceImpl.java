package services.impl;

import enums.Role;
import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import model.entities.*;
import model.pojos.User;
import repositories.RepositoryFactory;
import services.RoleService;
import services.UserService;
import util.CryptoUtils;
import util.Notification;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

    private static final Integer TOKEN_EXPIRATION_DURATION_MINUTES = 30;

    private RoleService roleService;

    @Inject
    public UserServiceImpl(RepositoryFactory repositoryFactory,
                           RoleService roleService) {
        // For testing purpose
        super(repositoryFactory);
        this.roleService = roleService;
    }

    public Long addUser(AddUserRequest addUserRequest) {
        Notification userRequestNotification = addUserRequest.validation();
        if (userRequestNotification.hasErrors())
            throw new UserRequestException(userRequestNotification.errorMessage());

        User user = new User();
        user.setName(addUserRequest.getName());
        user.setSurname(addUserRequest.getSurname());
        user.setRoleId(roleService.getRoleId(addUserRequest.getRole()));

        return  repositoryFactory.getUserRepository().addUser(user);
    }

    public void editUser(UUID userUuid, AddUserRequest addUserRequest) {
        repositoryFactory
                .getUserRepository()
                .editUser(userUuid, addUserRequest);
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
