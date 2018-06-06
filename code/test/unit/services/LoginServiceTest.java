package unit.services;

import model.entities.AddLoginRequest;
import model.pojos.Login;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import repositories.LoginRepository;
import repositories.RepositoryFactory;
import services.LoginService;
import services.impl.LoginServiceImpl;
import util.CryptoUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "jer0Me";
    private static final String PASSWORD = "mypassword";

    @Captor
    private ArgumentCaptor<Login> loginCaptor;

    private LoginService loginService;
    private LoginRepository loginRepository;

    public LoginServiceTest() {
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        loginRepository = mock(LoginRepository.class);
        when(repositoryFactory.getLoginRepository()).thenReturn(loginRepository);
        loginService = new LoginServiceImpl(repositoryFactory);
    }

    @Test
    public void shouldCreateALoginWithAPasswordCyphered() {
        UUID passwordSalt = UUID.randomUUID();
        AddLoginRequest addLoginRequest = new AddLoginRequest(USERNAME, PASSWORD);
        loginService.addLoginForUser(USER_ID, addLoginRequest);
        String passwordChecksum = CryptoUtils.generatePasswordCheckSum(PASSWORD, passwordSalt);
        verify(loginRepository).addLogin(any());
        // TODO
//        assertEquals(USER_ID, loginCaptor.getValue().getId());
//        assertEquals(passwordChecksum, loginCaptor.getValue().getPassword());
//        assertEquals(passwordSalt, loginCaptor.getValue().getPasswordSalt());
    }

    @Test
    public void shouldEditLoginWithPasswordCyphered() {
        String password = "newPassword";
        UUID passwordSalt = UUID.randomUUID();
        Long userId = 1L;

        when(loginRepository.getLoginPasswordSaltByUserId(userId)).thenReturn(Optional.of(passwordSalt));
        loginService.editLoginPassword(userId, password);

        String passwordChecksum = CryptoUtils.generatePasswordCheckSum(password, passwordSalt) ;
        verify(loginRepository).editLoginPassword(userId, passwordChecksum);
    }
}
