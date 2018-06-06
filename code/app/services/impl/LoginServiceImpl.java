package services.impl;

import exceptions.LoginNotExistException;
import model.entities.AddLoginRequest;
import model.pojos.Login;
import repositories.RepositoryFactory;
import services.LoginService;
import util.CryptoUtils;

import javax.inject.Inject;
import java.util.UUID;

public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    @Inject
    public LoginServiceImpl(RepositoryFactory repositoryFactory) {
        // For testing purpose
        super(repositoryFactory);
    }

    public void addLoginForUser(Long userId, AddLoginRequest addLoginRequest) {
        UUID passwordSalt = UUID.randomUUID();
        String passwordCheckSum = CryptoUtils.generatePasswordCheckSum(
                addLoginRequest.getPassword(), passwordSalt
        );

        Login login = new Login();
        login.setUsername(addLoginRequest.getUsername());
        login.setPassword(passwordCheckSum);
        login.setPasswordSalt(passwordSalt);
        login.setUserId(userId);

        repositoryFactory.getLoginRepository().addLogin(login);
    }

    public void editLoginPassword(Long userId, String password) {
        UUID passwordSalt = repositoryFactory
                .getLoginRepository()
                .getLoginPasswordSaltByUserId(userId)
                .orElseThrow(LoginNotExistException::new);

        repositoryFactory.getLoginRepository().editLoginPassword(
                userId, CryptoUtils.generatePasswordCheckSum(password, passwordSalt)
        );
    }
}
