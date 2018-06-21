package services.impl;

import enums.Role;
import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.EditUser;
import model.entities.AddUser;
import model.entities.NewToken;
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
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

    private static final Integer TOKEN_EXPIRATION_DURATION_MINUTES = 30;


    @Inject
    public UserServiceImpl(RepositoryFactory repositoryFactory) {
        // For testing purpose
        super(repositoryFactory);
    }

    public AddUserResponse addUser(UserRequest userRequest) {
        validateUserRequest(userRequest);

        return new AddUserResponse(
                repositoryFactory
                        .getUserRepository()
                        .addUser(
                                new AddUser(
                                        userRequest.getName(),
                                        userRequest.getSurname(),
                                        userRequest.getRole()
                                )
                        )
        );
    }

    private void validateUserRequest(UserRequest userRequest) {
        Notification userRequestNotification = userRequest.validation();

        if (userRequestNotification.hasErrors())
            throw new UserRequestException(userRequestNotification.errorMessage());

        Optional<UserResponse> userWithSameNameAndSurname = repositoryFactory.getUserRepository()
                .getUserByNameAndSurname(userRequest.getName(), userRequest.getSurname());

        if (userWithSameNameAndSurname.isPresent())
            throw new UserWithSameNameAndSurnameAlreadyExistException();
    }

    public void editUser(UUID userUuid, UserRequest userRequest) {
        repositoryFactory
                .getUserRepository()
                .editUser(new EditUser(
                        userUuid,
                        userRequest.getName(),
                        userRequest.getSurname(),
                        userRequest.getRole()
                ));
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
        repositoryFactory
                .getUserRepository()
                .updateUserTokenExpirationByUserId(userId, getTokenExpiration());
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
        repositoryFactory.getUserRepository().updateUserToken(
                userId, new NewToken(token, getTokenExpiration())
        );
        return token;
    }

    private LocalDateTime getTokenExpiration() {
        return LocalDateTime
                .now()
                .plusMinutes(TOKEN_EXPIRATION_DURATION_MINUTES);
    }
}
