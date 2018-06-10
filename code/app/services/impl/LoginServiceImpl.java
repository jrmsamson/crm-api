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

    public void addLoginForUser(AddEditLogin addEditLogin) {
        repositoryFactory
                .getLoginRepository()
                .addLogin(buildAddLogin(addEditLogin));
    }

    private Login buildAddLogin(AddEditLogin addEditLogin) {
        UUID passwordSalt = UUID.randomUUID();
        String passwordCheckSum = CryptoUtils.generatePasswordCheckSum(
                addEditLogin.getPassword(), passwordSalt
        );

        Login login = new Login();
        login.setUsername(addEditLogin.getUsername());
        login.setPassword(passwordCheckSum);
        login.setPasswordSalt(passwordSalt);
        login.setUserId(addEditLogin.getUserId());

        return login;
    }

    public void editLogin(AddEditLogin addEditLogin) {
        repositoryFactory
                .getLoginRepository()
                .editLogin(buildEditLogin(addEditLogin));
    }

    private Login buildEditLogin(AddEditLogin addEditLogin) {
        UUID passwordSalt = repositoryFactory
                .getLoginRepository()
                .getLoginPasswordSaltByUserId(addEditLogin.getUserId())
                .orElseThrow(LoginNotExistException::new);

        Login login = new Login();
        login.setUsername(addEditLogin.getUsername());
        login.setPassword(
                CryptoUtils.generatePasswordCheckSum(
                        addEditLogin.getPassword(),
                        passwordSalt
                )
        );

        login.setUserId(addEditLogin.getUserId());

        return login;
    }

    @Override
    public void deleteLoginByUserId(Long userId) {
        repositoryFactory
                .getLoginRepository()
                .deleteLoginByUserId(userId);
    }
}
