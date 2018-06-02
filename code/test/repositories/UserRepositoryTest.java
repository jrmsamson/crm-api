package repositories;

import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.UserData;
import model.entities.UserResponse;
import model.pojos.User;
import repositories.UserRepository;
import repositories.impl.UserRepositoryImpl;
import com.google.common.collect.ImmutableMap;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserRepositoryTest {

    private UserRepository userRepository;
    private Database database;
    private DSLContext dslContext;
    private Optional<UserResponse> userCreated;

    @Before
    public void setUp() {
        this.userRepository = new UserRepositoryImpl();
        this.database = Databases.createFrom(
                "org.postgresql.Driver",
                "jdbc:postgresql://postgres:5432/postgres",
                ImmutableMap.of(
                        "username", "postgres",
                        "password", "crmapi"
                )
        );
        this.dslContext = DSL.using(this.database.getConnection());

        Evolutions.applyEvolutions(database);

        setUpFixture();
    }

    private void setUpFixture() {
        User user = new User();
        user.setName("Jerome");
        user.setSurname("Samson");
        userRepository.addUser(dslContext, user).ifPresent(
                userCreatedUUID -> this.userCreated = userRepository.getUser(dslContext, userCreatedUUID)
        );
    }

    @Test
    public void shouldCreateANewUser() {
        assertTrue(userCreated.isPresent());
    }

    @Test
    public void shouldEditUser() {
        userCreated.ifPresent(userCreated -> {
            UserData user = new UserData("JR", "S");
            userRepository.editUser(dslContext, userCreated.getUuid(), user);
            UserResponse userEdited = userRepository.getUser(dslContext, userCreated.getUuid()).get();
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
        userRepository.addUser(dslContext, user);
    }

    @Test
    public void shouldDeleteUser() {
        userCreated.ifPresent(userCreated -> {
            userRepository.deleteUser(dslContext, userCreated.getUuid());
            Optional<UserResponse> user = userRepository.getUser(dslContext, userCreated.getUuid());
            assertFalse(user.isPresent());
        });
    }

    @Test
    public void shouldGetOnlyThoseUsersActive() {
        userCreated.ifPresent(userCreated -> {
            userRepository.deleteUser(dslContext, userCreated.getUuid());
            User newUser = new User();
            newUser.setName("JR");
            newUser.setSurname("SAM");
            UUID newUserUUID = userRepository.addUser(dslContext, newUser).get();
            List<UserResponse> users = userRepository.getUsersActive(dslContext);

            assertEquals(1, users.size());
            assertEquals(users.get(0).getUuid(), newUserUUID);
        });
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }
}
