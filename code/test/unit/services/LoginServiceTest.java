package unit.services;

import enums.Role;
import exceptions.IncorrectUsernameOrPasswordException;
import model.entities.*;
import model.pojos.Login;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import repositories.LoginRepository;
import repositories.RepositoryFactory;
import services.LoginService;
import services.UserService;
import services.impl.LoginServiceImpl;
import util.CryptoUtils;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "jer0Me";
    private static final String PASSWORD = "mypassword";
    private static final UUID PASSWORD_SALT = UUID.randomUUID();

    private LoginService loginService;
    private UserService userService;
    private LoginRepository loginRepository;

    public LoginServiceTest() {
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        loginRepository = mock(LoginRepository.class);
        userService = mock(UserService.class);
        when(repositoryFactory.getLoginRepository()).thenReturn(loginRepository);
        loginService = new LoginServiceImpl(repositoryFactory, userService);
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
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);
        String passwordChecksum = CryptoUtils.generatePasswordCheckSum(PASSWORD, PASSWORD_SALT);
        when(loginRepository.getLoginByUsername(USERNAME)).thenReturn(
                Optional.of(new LoginResponse(1L, passwordChecksum, PASSWORD_SALT))
        );
        UserSession userSession = loginService.login(loginRequest);
        assertNotNull(userSession);
    }

    @Test(expected = IncorrectUsernameOrPasswordException.class)
    public void shouldThrowIncorrectUsernameOrPasswordExceptionWhenPasswordDoesNotMatch() {
        setUpLoginFixture();
        loginService.login(new LoginRequest(USERNAME, "OTHER PASSWORD"));
    }

    @Test(expected = IncorrectUsernameOrPasswordException.class)
    public void shouldThrowIncorrectUsernameOrPasswordExceptionWhenTheLoginDoesNotExist() {
        when(loginRepository.getLoginByUsername(USERNAME))
                .thenReturn(Optional.empty());
        loginService.login(new LoginRequest(USERNAME, PASSWORD));
    }

    @Test
    public void shouldAddLoginWithAPasswordCyphered() {
        AddLoginRequest addLoginRequest = new AddLoginRequest(USERNAME, PASSWORD, USER_ID);
        loginService.addLoginForUser(addLoginRequest);
        verify(loginRepository).addLogin(any());
    }

    @Test
    public void shouldEditLoginWithPasswordCyphered() {
        String password = "newPassword";
        UUID passwordSalt = UUID.randomUUID();
        Long userId = 1L;

        when(loginRepository.getLoginPasswordSaltByUserId(userId)).thenReturn(Optional.of(passwordSalt));
        loginService.editLoginPassword(new EditPasswordRequest(userId, password));

        String passwordChecksum = CryptoUtils.generatePasswordCheckSum(password, passwordSalt) ;
        verify(loginRepository).editLoginPassword(userId, passwordChecksum);
    }
}
