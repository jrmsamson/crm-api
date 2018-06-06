package unit.services;

import enums.Role;
import exceptions.IncorrectUsernameOrPasswordException;
import model.entities.LoginRequest;
import model.entities.LoginResponse;
import org.junit.Test;
import repositories.LoginRepository;
import repositories.RepositoryFactory;
import services.AuthenticationService;
import services.UserService;
import services.impl.AuthenticationServiceImpl;
import util.CryptoUtils;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    private static final String USERNAME = "jer0Me";
    private static final String PASSWORD = "mypassword";
    private static final UUID PASSWORD_SALT = UUID.randomUUID();

    private AuthenticationService authenticationService;
    private LoginRepository loginRepository;
    private UserService userService;

    public AuthenticationServiceTest() {
        loginRepository = mock(LoginRepository.class);
        userService = mock(UserService.class);
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        when(repositoryFactory.getLoginRepository()).thenReturn(loginRepository);
        authenticationService = new AuthenticationServiceImpl(repositoryFactory);
    }

    private void setUpLoginFixture() {
        LoginResponse loginResponse = new LoginResponse(
                1L,
                CryptoUtils.generatePasswordCheckSum(PASSWORD, PASSWORD_SALT),
                PASSWORD_SALT
        );
        when(loginRepository.getLoginByUsername(USERNAME))
                .thenReturn(Optional.of(loginResponse));
        when(userService.getUserRole(1L)).thenReturn(Role.USER);
    }

    @Test
    public void shouldRetrieveATokenIfLoginIsSuccessfull() {
        setUpLoginFixture();
        // TODO
//        String token = authenticationService.login(new LoginRequest(USERNAME, PASSWORD));
    }

    @Test(expected = IncorrectUsernameOrPasswordException.class)
    public void shouldThrowIncorrectUsernameOrPasswordExceptionWhenPasswordDoesNotMatch() {
        setUpLoginFixture();
        authenticationService.login(new LoginRequest(USERNAME, "OTHER PASSWORD"));
    }

    @Test(expected = IncorrectUsernameOrPasswordException.class)
    public void shouldThrowIncorrectUsernameOrPasswordExceptionWhenTheLoginDoesNotExist() {
        when(loginRepository.getLoginByUsername(USERNAME))
                .thenReturn(Optional.empty());
        authenticationService.login(new LoginRequest(USERNAME, PASSWORD));
    }

}
