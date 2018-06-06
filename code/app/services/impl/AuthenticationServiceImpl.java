package services.impl;

import exceptions.IncorrectUsernameOrPasswordException;
import model.entities.LoginRequest;
import model.entities.LoginResponse;
import model.entities.UpdateUserTokenRequest;
import repositories.RepositoryFactory;
import services.AuthenticationService;
import util.CryptoUtils;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class AuthenticationServiceImpl extends BaseServiceImpl implements AuthenticationService {

    @Inject
    public AuthenticationServiceImpl(RepositoryFactory repositoryFactory) {
        // For testing purpose
        super(repositoryFactory);
    }

    public String login(LoginRequest loginRequest) {
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

        return token;
    }
}
