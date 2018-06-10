package integration.repositories;

import enums.Role;
import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.requests.UpdateUserTokenExpirationRequest;
import model.entities.requests.UpdateUserTokenRequest;
import model.entities.responses.UserResponse;
import model.entities.responses.UserTokenResponse;
import model.pojos.User;
import org.jooq.DSLContext;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import repositories.UserRepository;
import repositories.impl.UserRepositoryImpl;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.evolutions.Evolutions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserRepositoryTest {

    private static final Long USER_ID = 1L;
    private static final Integer USER_ROLE_ID = 2;

    private UserRepository userRepository;
    private Database database;
    private Optional<UserResponse> userCreated;

    public UserRepositoryTest() {
        Application application = new GuiceApplicationBuilder().build();
        database = application.injector().instanceOf(Database.class);
        userRepository = new UserRepositoryImpl();
        DSLContext dslContext = DSL.using(this.database.getConnection());
        userRepository.setDslContext(dslContext);
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
        setUpFixture();
    }

    private void setUpFixture() {
        User user = new User();
        user.setName("Jerome");
        user.setSurname("Samson");
        user.setRoleId(USER_ROLE_ID);
        this.userCreated = userRepository.getUserByUuid(
                userRepository.addUser(user)
        );
    }

    @Test
    public void shouldCreateANewUser() {
        assertTrue(userCreated.isPresent());
    }

    @Test
    public void shouldEditUser() {
        userCreated.ifPresent(userCreated -> {
            User user = new User();
            user.setName("JR");
            user.setSurname("R");
            user.setRoleId(2);
            user.setUuid(userCreated.getUuid());
            userRepository.editUser(user);
            UserResponse userEdited = userRepository.getUserByUuid(userCreated.getUuid()).get();
            assertEquals(userCreated.getUuid(), userEdited.getUuid());
            assertEquals(user.getName(), userEdited.getName());
            assertEquals(user.getSurname(), userEdited.getSurname());
        });
    }

    @Test(expected = UserWithSameNameAndSurnameAlreadyExistException.class)
    public void shouldNotBeAbleToCreateANewUserWithTheSameNameAndSurname() {
        User user = new User();
        user.setName("Jerome");
        user.setSurname("Samson");
        user.setRoleId(USER_ROLE_ID);
        userRepository.addUser(user);
    }

    @Test
    public void shouldDeleteUser() {
        userCreated.ifPresent(userCreated -> {
            userRepository.deleteUser(userCreated.getUuid());
            Optional<UserResponse> user = userRepository.getUserByUuid(userCreated.getUuid());
            assertFalse(user.isPresent());
        });
    }

    @Test
    public void shouldGetOnlyThoseUsersActive() {
        userCreated.ifPresent(userCreated -> {
            userRepository.deleteUser(userCreated.getUuid());
            User user = new User();
            user.setName("JR");
            user.setSurname("SAM");
            user.setRoleId(USER_ROLE_ID);
            userRepository.addUser(user);
            List<UserResponse> users = userRepository.getUsersActive();
            assertEquals(1, users.size());
            assertEquals(users.get(0).getName(), "JR");
            assertEquals(users.get(0).getSurname(), "SAM");
        });
    }

    @Test
    public void shouldReturnTheUserRole() {
        Optional<Role> userRole = userRepository.getUserRoleByUserId(USER_ID);
        assertTrue(userRole.isPresent());
    }

    @Test
    public void shouldUpdateUserToken() {
        String token = "mytoken";
        LocalDateTime tokenExpiration = LocalDateTime.now();
        userRepository.updateUserToken(new UpdateUserTokenRequest(USER_ID, token, tokenExpiration));
        UserTokenResponse userTokenResponse = userRepository.getUserToken(USER_ID).get();
        assertEquals(token, userTokenResponse.getToken());
        assertEquals(tokenExpiration, userTokenResponse.getTokenExpiration());
    }

    @Test
    public void shouldUpdateUserTokenExpiration() {
        LocalDateTime tokenExpiration = LocalDateTime.now();
        userRepository.updateUserTokenExpiration(new UpdateUserTokenExpirationRequest(USER_ID, tokenExpiration));
        UserTokenResponse userTokenResponse = userRepository.getUserToken(USER_ID).get();
        assertEquals(tokenExpiration, userTokenResponse.getTokenExpiration());
    }

    @Test
    public void shouldGetUserIdByUuid() {
        userRepository.getUserIdByUuid(userCreated.get().getUuid());
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }
}
