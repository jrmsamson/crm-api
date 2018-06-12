package services.impl;

import enums.Role;
import exceptions.RoleDoesNotExistException;
import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.requests.UserRequest;
import model.entities.responses.AddUserResponse;
import model.entities.responses.UserResponse;
import model.entities.responses.UserTokenResponse;
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

    public AddUserResponse addUser(UserRequest userRequest) {
        Notification userRequestNotification = userRequest.validation();
        if (userRequestNotification.hasErrors())
            throw new UserRequestException(userRequestNotification.errorMessage());

        User user = buildUser(userRequest);

        if (repositoryFactory.getUserRepository()
                .existAndUserWithTheSameNameAndSurname(user))
            throw new UserWithSameNameAndSurnameAlreadyExistException();

        return new AddUserResponse(
                repositoryFactory
                .getUserRepository()
                .addUser(user)
        );
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
                .editUserByUuid(user);
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
                .deleteUserByUuid(userUuid);
    }

    public List<UserResponse> getUsersActive() {
        return repositoryFactory
                .getUserRepository()
                .getUsersActive(currentUserId);
    }

    public Role getUserRole(Long userId) {
        return repositoryFactory
                .getUserRepository()
                .getUserRoleByUserId(userId)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public UserTokenResponse getUserToken(Long userId) {
        return repositoryFactory
                .getUserRepository()
                .getUserTokenByUserId(userId)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public void removeCurrentUserToken() {
        repositoryFactory
                .getUserRepository()
                .removeUserToken(currentUserId);
    }

    public void renewUserToken(Long userId) {
        User user = new User();
        user.setTokenExpiration(getTokenExpiration());
        user.setId(userId);
        repositoryFactory
                .getUserRepository()
                .updateUserTokenExpirationByUserId(user);
    }

    public Long getUserIdByUuid(UUID userUuid) {
        return repositoryFactory
                .getUserRepository()
                .getUserIdByUuid(userUuid)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public UserResponse getUserByUuid(UUID userUuid) {
        return repositoryFactory
                .getUserRepository()
                .getUserByUuid(userUuid)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public String buildUserToken(Long userId) {
        String token = CryptoUtils.generateSecureRandomToken();
        repositoryFactory.getUserRepository().updateUserTokenByUserId(
                buildUserToken(userId, token)
        );
        return token;
    }


    private User buildUserToken(Long userId, String token) {
        User user = new User();
        user.setId(userId);
        user.setToken(token);
        user.setTokenExpiration(getTokenExpiration());
        return user;
    }

    private LocalDateTime getTokenExpiration() {
        return LocalDateTime
                .now()
                .plusMinutes(TOKEN_EXPIRATION_DURATION_MINUTES);
    }


}
