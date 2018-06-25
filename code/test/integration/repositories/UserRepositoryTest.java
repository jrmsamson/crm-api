package integration.repositories;

import exceptions.UserRepositoryException;
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
import repositories.UserRepository;
import repositories.impl.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserRepositoryTest {

    private static final Integer USER_ROLE_ID = 2;
    private static final int ADMIN_ROLE_ID = 1;

    private UserRepository userRepository;
    private Database database;
    private User userAdded;

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
        User user = new User();
        user.setName("Jerome");
        user.setSurname("Samson");
        user.setRoleId(USER_ROLE_ID);
        this.userAdded = userRepository.getByUuid(
                userRepository.add(user)
        ).get();
    }

    @Test
    public void shouldAddUser() {
        assertNotNull(userAdded);
    }

    @Test(expected = UserRepositoryException.class)
    public void shouldThrowAnExceptionWhenNewUserNameIsNull() {
        User user = new User();
        user.setSurname("Samson");
        user.setRoleId(ADMIN_ROLE_ID);
        userRepository.add(user);
    }

    @Test(expected = UserRepositoryException.class)
    public void shouldThrowAnExceptionWhenNewUserSurnameIsNull() {
        User user = new User();
        user.setName("Jerome");
        user.setRoleId(ADMIN_ROLE_ID);
        userRepository.add(user);
    }

    @Test
    public void shouldUpdateUser() {
        LocalDateTime tokenExpiration = LocalDateTime.now();
        User user = new User(userAdded);
        user.setId(100L);
        user.setName("JR");
        user.setSurname("SAM");
        user.setRoleId(ADMIN_ROLE_ID);
        user.setToken("Mytoken");
        user.setTokenExpiration(tokenExpiration);

        userRepository.update(user);
        Optional<User> userEdited = userRepository.getByUuid(userAdded.getUuid());
        assertTrue(userEdited.isPresent());
        assertEquals(userAdded.getId(), userEdited.get().getId());
        assertEquals("JR", userEdited.get().getName());
        assertEquals("SAM", userEdited.get().getSurname());
        assertEquals(ADMIN_ROLE_ID, userEdited.get().getRoleId(), 0);
        assertEquals("Mytoken", userEdited.get().getToken());
        assertEquals(tokenExpiration, userEdited.get().getTokenExpiration());
    }

    @Test
    public void shouldDeleteUser() {
        userRepository.deleteByUuid(userAdded.getUuid());
        Optional<User> user = userRepository.getByUuid(userAdded.getUuid());
        assertFalse(user.isPresent());
    }

    @Test
    public void shouldGetOnlyThoseUsersActive() {
        userRepository.deleteByUuid(userAdded.getUuid());
        User user = new User();
        user.setName("JR");
        user.setSurname("SAM");
        user.setRoleId(USER_ROLE_ID);
        userRepository.add(user);
        List<User> users = userRepository.getActive(1L);
        assertEquals(1, users.size());
        assertEquals(users.get(0).getName(), user.getName());
        assertEquals(users.get(0).getSurname(), user.getSurname());
    }

    @Test
    public void shouldGetUserByNameAndSurname() {
        assertTrue(
                userRepository.getByNameAndSurname(
                        userAdded.getName(), userAdded.getSurname()
                ).isPresent()
        );
    }

    @Test
    public void shouldGetUserById() {
        assertTrue(
                userRepository.getById(userAdded.getId()).isPresent()
        );
    }

}
