package services.impl;

import exceptions.RoleDoesNotExistException;
import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.requests.UserRequest;
import model.entities.responses.AddUserResponse;
import model.entities.responses.UserResponse;
import model.entities.responses.UserToken;
import model.pojos.User;
import repositories.RepositoryFactory;
import services.UserService;
import util.CryptoUtils;
import util.Notification;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

    private static final Integer TOKEN_EXPIRATION_DURATION_MINUTES = 30;

    @Inject
    public UserServiceImpl(RepositoryFactory repositoryFactory) {
        // For testing purpose
        super(repositoryFactory);
    }

    public AddUserResponse addUser(UserRequest userRequest) {
        validateUserRequest(userRequest);

        User user = new User();
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setRoleId(getRoleId(userRequest));

        return new AddUserResponse(
                repositoryFactory
                        .getUserRepository()
                        .add(user)
        );
    }

    private Integer getRoleId(UserRequest userRequest) {
        return repositoryFactory
                .getRoleRepository()
                .getByName(userRequest.getRole())
                .orElseThrow(RoleDoesNotExistException::new)
                .getId();
    }

    public void updateUser(UUID userUuid, UserRequest userRequest) {
        User user = getUserByUuidFromRepository(userUuid);
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setRoleId(getUserRole(userRequest.getRole()));
        repositoryFactory
                .getUserRepository()
                .update(user);
    }

    private Integer getUserRole(String roleName) {
        return repositoryFactory
                .getRoleRepository()
                .getByName(roleName)
                .orElseThrow(RoleDoesNotExistException::new)
                .getId();
    }


    private void validateUserRequest(UserRequest userRequest) {
        Notification userRequestNotification = userRequest.validation();

        if (userRequestNotification.hasErrors())
            throw new UserRequestException(userRequestNotification.errorMessage());

        Optional<User> userWithSameNameAndSurname = repositoryFactory.getUserRepository()
                .getByNameAndSurname(userRequest.getName(), userRequest.getSurname());

        if (userWithSameNameAndSurname.isPresent())
            throw new UserWithSameNameAndSurnameAlreadyExistException();
    }

    public void deleteUser(UUID userUuid) {
        repositoryFactory
                .getUserRepository()
                .deleteByUuid(userUuid);
    }

    public List<UserResponse> getUsersActive() {
        return repositoryFactory
                .getUserRepository()
                .getActive(currentUserId)
                .stream()
                .map(
                        user -> new UserResponse(
                                user, getUserRoleByRoleId(user.getRoleId())
                        )
                )
                .collect(Collectors.toList());
    }

    public UserToken getUserTokenByUserId(Long userId) {
        User user = getUserById(userId);

        return new UserToken(
                user.getToken(),
                user.getTokenExpiration()
        );
    }

    public void removeUserToken(Long userId) {
        User user = getUserById(userId);
        user.setToken(null);
        user.setTokenExpiration(null);
        repositoryFactory
                .getUserRepository()
                .update(user);
    }

    public void renewUserToken(Long userId) {
        User user = getUserById(userId);
        user.setTokenExpiration(getTokenExpiration());
        repositoryFactory
                .getUserRepository()
                .update(user);
    }

    private User getUserById(Long userId) {
        return repositoryFactory
                .getUserRepository()
                .getById(userId)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public Long getUserIdByUuid(UUID userUuid) {
        return getUserByUuidFromRepository(userUuid)
                .getId();
    }

    public UserResponse getUserByUuid(UUID userUuid) {
        User user = getUserByUuidFromRepository(userUuid);

        return new UserResponse(user, getUserRoleByRoleId(user.getRoleId()));
    }

    public String getUserRoleByUserId(Long userId) {
        return getUserRoleByRoleId(
                getUserById(userId).getRoleId()
        );
    }

    private String getUserRoleByRoleId(Integer roleId) {
        return repositoryFactory
                .getRoleRepository()
                .getById(roleId)
                .orElseThrow(RoleDoesNotExistException::new)
                .getName();
    }

    private User getUserByUuidFromRepository(UUID userUuid) {
        return repositoryFactory
                .getUserRepository()
                .getByUuid(userUuid)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public String setUserTokenByUserId(Long userId) {
        User user = getUserById(userId);

        String token = CryptoUtils.generateSecureRandomToken();
        user.setToken(token);
        user.setTokenExpiration(getTokenExpiration());

        repositoryFactory
                .getUserRepository()
                .update(user);

        return token;
    }

    private LocalDateTime getTokenExpiration() {
        return LocalDateTime
                .now()
                .plusMinutes(TOKEN_EXPIRATION_DURATION_MINUTES);
    }
}
