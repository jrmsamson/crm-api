package integration.repositories;

import model.entities.*;
import model.pojos.Login;
import model.pojos.User;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import repositories.LoginRepository;
import repositories.UserRepository;
import repositories.impl.LoginRepositoryImpl;
import repositories.impl.UserRepositoryImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginRepositoryTest  {

    private static final String USERNAME = "jer0Me";
    private static final Long LOGIN_USER_ID_CREATED = 2L;
    private static final UUID PASSWORD_SALT = UUID.randomUUID();

    private LoginRepository loginRepository;
    private UserRepository userRepository;
    private Database database;
    private Optional<LoginResponse> loginPasswordCreated;

    public LoginRepositoryTest() {
        Application application = new GuiceApplicationBuilder().build();
        database = application.injector().instanceOf(Database.class);
        loginRepository = new LoginRepositoryImpl();
        DSLContext dslContext = DSL.using(this.database.getConnection());
        loginRepository.setDslContext(dslContext);
        userRepository = new UserRepositoryImpl();
        userRepository.setDslContext(dslContext);
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
        User user = new User();
        user.setName("Jerome");
        user.setSurname("Samson");
        user.setRoleId(1);
        userRepository.addUser(user);
        setUpFixture();
    }

    private void setUpFixture() {
        Login login = new Login();
        login.setUsername(USERNAME);
        login.setPassword("mypassword");
        login.setPasswordSalt(PASSWORD_SALT);
        login.setUserId(LOGIN_USER_ID_CREATED);
        loginRepository.addLogin(login);
        loginPasswordCreated = loginRepository.getLoginByUsername(USERNAME);
    }

    @Test
    public void shouldCreateANewLogin() {
        assertTrue(loginPasswordCreated.isPresent());
    }

    @Test
    public void shouldEditLogin() {
        Login login = new Login();
        login.setUsername("jrm");
        login.setPassword("password");
        login.setUserId(LOGIN_USER_ID_CREATED);
        loginRepository.editLogin(login);

        LoginResponse loginResponse =
                loginRepository.getLoginByUsername("jrm").get();

        assertEquals(login.getPassword(), loginResponse.getPassword());
    }


    @Test
    public void shouldDeleteLoginByUserId() {
        loginRepository.deleteLoginByUserId(LOGIN_USER_ID_CREATED);
        Optional<LoginResponse> loginPasswordResponse =
                loginRepository.getLoginByUsername(USERNAME);
        assertFalse(loginPasswordResponse.isPresent());
    }

    @Test
    public void shouldGetLoginPasswordSaltByUserId() {
        UUID passwordSalt = loginRepository.getLoginPasswordSaltByUserId(LOGIN_USER_ID_CREATED).get();
        assertEquals(PASSWORD_SALT, passwordSalt);
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }
}
