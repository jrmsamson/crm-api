package integration.repositories;

import enums.Role;
import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.AddUserRequest;
import model.entities.UpdateUserTokenRequest;
import model.entities.UserResponse;
import model.entities.UserTokenResponse;
import model.pojos.User;
import org.jooq.DSLContext;
import play.Application;
import play.Logger;
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
        this.userCreated = userRepository.getUserById(
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
            AddUserRequest user = new AddUserRequest("JR", "S", Role.USER, null, null);
            userRepository.editUser(userCreated.getUuid(), user);
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
        Optional<Role> userRole = userRepository.getUserRoleByUserId(1L);
        assertTrue(userRole.isPresent());
    }

    @Test
    public void shouldUpdateUserToken() {
        Long userId = 1L;
        String token = "mytoken";
        LocalDateTime tokenExpiration = LocalDateTime.now();
        userRepository.updateUserToken(new UpdateUserTokenRequest(userId, token, tokenExpiration));
        UserTokenResponse userTokenResponse = userRepository.getUserToken(userId).get();
        assertEquals(token, userTokenResponse.getToken());
        assertEquals(tokenExpiration, userTokenResponse.getTokenExpiration());
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }
}
