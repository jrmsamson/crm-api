package services.impl;

import exceptions.IncorrectUsernameOrPasswordException;
import exceptions.LoginNotExistException;
import model.entities.*;
import model.pojos.Login;
import repositories.RepositoryFactory;
import services.LoginService;
import services.UserService;
import util.CryptoUtils;

import javax.inject.Inject;
import java.util.UUID;

public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    private UserService userService;

    @Inject
    public LoginServiceImpl(RepositoryFactory repositoryFactory, UserService userService) {
        // For testing purpose
        super(repositoryFactory);
        this.userService = userService;
    }

    public UserSession login(LoginRequest loginRequest) {
        LoginResponse loginResponse = repositoryFactory
                .getLoginRepository()
                .getLoginByUsername(loginRequest.getUsername())
                .orElseThrow(IncorrectUsernameOrPasswordException::new);

        validatePassword(loginRequest, loginResponse);

        return buildUserSession(loginResponse);
    }

    private UserSession buildUserSession(LoginResponse loginResponse) {
        return new UserSession(
                loginResponse.getUserId(),
                userService.getUserRole(loginResponse.getUserId()),
                userService.buildUserToken(loginResponse.getUserId())
        );
    }

    private void validatePassword(LoginRequest loginRequest, LoginResponse loginResponse) {
        String passwordChecksum = CryptoUtils.generatePasswordCheckSum(loginRequest.getPassword(), loginResponse.getPasswordSalt());

        if (!passwordChecksum.equals(loginResponse.getPassword())) {
            throw new IncorrectUsernameOrPasswordException();
        }
    }

    public void logout() {
        userService.removeCurrentUserToken();
    }

    public void addLoginForUser(AddLoginRequest addLoginRequest) {
        UUID passwordSalt = UUID.randomUUID();
        String passwordCheckSum = CryptoUtils.generatePasswordCheckSum(
                addLoginRequest.getPassword(), passwordSalt
        );

        Login login = new Login();
        login.setUsername(addLoginRequest.getUsername());
        login.setPassword(passwordCheckSum);
        login.setPasswordSalt(passwordSalt);
        login.setUserId(addLoginRequest.getUserId());

        repositoryFactory.getLoginRepository().addLogin(login);
    }

    public void editLoginPassword(EditPasswordRequest editPasswordRequest) {
        UUID passwordSalt = repositoryFactory
                .getLoginRepository()
                .getLoginPasswordSaltByUserId(editPasswordRequest.getUserId())
                .orElseThrow(LoginNotExistException::new);

        repositoryFactory.getLoginRepository().editLoginPassword(
                editPasswordRequest.getUserId(),
                CryptoUtils.generatePasswordCheckSum(
                        editPasswordRequest.getPassword(),
                        passwordSalt
                )
        );
    }
}
