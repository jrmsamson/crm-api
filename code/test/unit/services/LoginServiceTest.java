package unit.services;

import enums.Role;
import exceptions.IncorrectUsernameOrPasswordException;
import model.entities.requests.AddEditLoginRequest;
import model.entities.requests.LoginRequest;
import model.entities.responses.LoginResponse;
import model.entities.responses.UserSessionResponse;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "jer0Me";
    private static final String PASSWORD = "mypassword";
    private static final UUID PASSWORD_SALT = UUID.randomUUID();

    private LoginService loginService;
    private UserService userService;
    private LoginRepository loginRepository;

    @Captor
    private ArgumentCaptor<Login> loginCaptor;

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
        UserSessionResponse userSessionResponse = loginService.login(loginRequest);
        assertNotNull(userSessionResponse);
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
        AddEditLoginRequest addEditLoginRequest = new AddEditLoginRequest(USERNAME, PASSWORD, USER_ID);
        loginService.addLoginForUser(addEditLoginRequest);
        verify(loginRepository).addLogin(any());
    }

    @Test
    public void shouldEditLoginWithPasswordCyphered() {
        String username = "jerome";
        String password = "newPassword";
        UUID passwordSalt = UUID.randomUUID();
        Long userId = 1L;
        when(loginRepository.getLoginPasswordSaltByUserId(userId)).thenReturn(Optional.of(passwordSalt));
        loginService.editLogin(new AddEditLoginRequest(username, password, userId));

        verify(loginRepository).editLogin(loginCaptor.capture());
        Login login = loginCaptor.getValue();

        String passwordChecksum = CryptoUtils.generatePasswordCheckSum(password, passwordSalt) ;
        assertEquals(username, login.getUsername());
        assertEquals(passwordChecksum, login.getPassword());
        assertEquals(userId, login.getUserId());
    }

    @Test
    public void shouldDeleteLoginByUserId() {
        Long userId = 1L;
        loginService.deleteLoginByUserId(userId);
        verify(loginRepository).deleteLoginByUserId(userId);
    }
}
