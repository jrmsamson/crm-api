package integration.repositories;

import enums.Role;
import model.entities.EditUser;
import model.entities.AddUser;
import model.entities.NewToken;
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

import static org.junit.Assert.*;

public class UserRepositoryTest {

    private static final Long USER_ID = 1L;
    private static final Integer USER_ROLE_ID = 2;

    private UserRepository userRepository;
    private Database database;
    private UserResponse userCreated;

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

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }

    private void setUpFixture() {
        AddUser addUser = new AddUser(
                "Jerome", "Samson", Role.USER
        );
        User user = new User();
        user.setName("Jerome");
        user.setSurname("Samson");
        user.setRoleId(USER_ROLE_ID);
        this.userCreated = userRepository.getUserByUuid(
                userRepository.addUser(addUser)
        ).get();
    }

    @Test
    public void shouldCreateANewUser() {
        assertNotNull(userCreated);
    }

    @Test
    public void shouldEditUser() {
        EditUser user = new EditUser(userCreated.getUuid(), "JR", "R", Role.USER);
        userRepository.editUser(user);
        UserResponse userEdited = userRepository.getUserByUuid(userCreated.getUuid()).get();
        assertEquals(userCreated.getUuid(), userEdited.getUuid());
        assertEquals(user.getName(), userEdited.getName());
        assertEquals(user.getSurname(), userEdited.getSurname());
    }

    @Test
    public void shouldDeleteUser() {
        userRepository.deleteUserByUuid(userCreated.getUuid());
        Optional<UserResponse> user = userRepository.getUserByUuid(userCreated.getUuid());
        assertFalse(user.isPresent());
    }

    @Test
    public void shouldGetOnlyThoseUsersActive() {
        userRepository.deleteUserByUuid(userCreated.getUuid());
        AddUser user = new AddUser("JR", "SAM", Role.USER);
        userRepository.addUser(user);
        List<UserResponse> users = userRepository.getUsersActive(1L);
        assertEquals(1, users.size());
        assertEquals(users.get(0).getName(), "JR");
        assertEquals(users.get(0).getSurname(), "SAM");
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
        userRepository.updateUserToken(USER_ID, new NewToken(token, tokenExpiration));
        UserTokenResponse userTokenResponse = userRepository.getUserTokenByUserId(USER_ID).get();
        assertEquals(token, userTokenResponse.getToken());
        assertEquals(tokenExpiration, userTokenResponse.getTokenExpiration());
    }

    @Test
    public void shouldUpdateUserTokenExpiration() {
        LocalDateTime tokenExpiration = LocalDateTime.now();
        userRepository.updateUserTokenExpirationByUserId(USER_ID, tokenExpiration);
        UserTokenResponse userTokenResponse = userRepository.getUserTokenByUserId(USER_ID).get();
        assertEquals(tokenExpiration, userTokenResponse.getTokenExpiration());
    }

    @Test
    public void shouldGetUserIdByUuid() {
        Optional<Long> userId = userRepository.getUserIdByUuid(userCreated.getUuid());
        assertTrue(userId.isPresent());
    }

    @Test
    public void shouldReturnUserGottenByNameAndSurname() {
        assertTrue(
                userRepository.getUserByNameAndSurname(
                        userCreated.getName(), userCreated.getSurname()
                ).isPresent()
        );
    }

}
