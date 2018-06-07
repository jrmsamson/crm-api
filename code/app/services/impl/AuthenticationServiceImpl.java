package services.impl;

import enums.Role;
import exceptions.IncorrectUsernameOrPasswordException;
import model.entities.LoginRequest;
import model.entities.LoginResponse;
import model.entities.UpdateUserTokenRequest;
import model.entities.UserSession;
import repositories.RepositoryFactory;
import services.AuthenticationService;
import services.UserService;
import util.CryptoUtils;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class AuthenticationServiceImpl extends BaseServiceImpl implements AuthenticationService {

    private UserService userService;

    @Inject
    public AuthenticationServiceImpl(RepositoryFactory repositoryFactory, UserService userService) {
        // For testing purpose
        super(repositoryFactory);
        this.userService = userService;
    }

    public UserSession login(LoginRequest loginRequest) {
        LoginResponse loginResponse = repositoryFactory
                .getLoginRepository()
                .getLoginByUsername(loginRequest.getUsername())
                .orElseThrow(IncorrectUsernameOrPasswordException::new);

        String passwordChecksum = CryptoUtils.generatePasswordCheckSum(loginRequest.getPassword(), loginResponse.getPasswordSalt());

        if (!passwordChecksum.equals(loginResponse.getPassword())) {
            throw new IncorrectUsernameOrPasswordException();
        }

        String token = CryptoUtils.generateSecureRandomToken();
        LocalDateTime tokenExpiration = LocalDateTime.now();
        tokenExpiration = tokenExpiration.plusMinutes(30);
        repositoryFactory.getUserRepository().updateUserToken(
                new UpdateUserTokenRequest(loginResponse.getUserId(), token, tokenExpiration));

        Role userRole = userService.getUserRole(loginResponse.getUserId());

        return new UserSession(loginResponse.getUserId(), userRole, token);
    }

    public void logout() {
        userService.removeCurrentUserToken();
    }
}
