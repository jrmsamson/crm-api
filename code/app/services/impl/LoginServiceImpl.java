package services.impl;

import exceptions.IncorrectUsernameOrPasswordException;
import exceptions.LoginNotExistException;
import model.entities.requests.AddEditLoginRequest;
import model.entities.requests.LoginRequest;
import model.entities.responses.LoginResponse;
import model.entities.responses.UserSessionResponse;
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

    public UserSessionResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = repositoryFactory
                .getLoginRepository()
                .getLoginByUsername(loginRequest.getUsername())
                .orElseThrow(IncorrectUsernameOrPasswordException::new);

        validatePassword(loginRequest, loginResponse);

        return buildUserSession(loginResponse);
    }

    private UserSessionResponse buildUserSession(LoginResponse loginResponse) {
        return new UserSessionResponse(
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

    public void addLoginForUser(AddEditLoginRequest addEditLoginRequest) {
        repositoryFactory
                .getLoginRepository()
                .addLogin(buildAddLogin(addEditLoginRequest));
    }

    private Login buildAddLogin(AddEditLoginRequest addEditLoginRequest) {
        UUID passwordSalt = UUID.randomUUID();
        String passwordCheckSum = CryptoUtils.generatePasswordCheckSum(
                addEditLoginRequest.getPassword(), passwordSalt
        );

        Login login = new Login();
        login.setUsername(addEditLoginRequest.getUsername());
        login.setPassword(passwordCheckSum);
        login.setPasswordSalt(passwordSalt);
        login.setUserId(addEditLoginRequest.getUserId());

        return login;
    }

    public void editLogin(AddEditLoginRequest addEditLoginRequest) {
        repositoryFactory
                .getLoginRepository()
                .editLogin(buildEditLogin(addEditLoginRequest));
    }

    private Login buildEditLogin(AddEditLoginRequest addEditLoginRequest) {
        UUID passwordSalt = repositoryFactory
                .getLoginRepository()
                .getLoginPasswordSaltByUserId(addEditLoginRequest.getUserId())
                .orElseThrow(LoginNotExistException::new);

        Login login = new Login();
        login.setUsername(addEditLoginRequest.getUsername());
        login.setPassword(
                CryptoUtils.generatePasswordCheckSum(
                        addEditLoginRequest.getPassword(),
                        passwordSalt
                )
        );

        login.setUserId(addEditLoginRequest.getUserId());

        return login;
    }

    @Override
    public void deleteLoginByUserId(Long userId) {
        repositoryFactory
                .getLoginRepository()
                .deleteLoginByUserId(userId);
    }
}
