package repositories;

import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.UserRequest;
import model.entities.UserResponse;
import model.pojos.User;
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
    private Optional<UserResponse> userCreated;

    @Before
    public void setUp() {
        this.database = Databases.createFrom(
                "org.postgresql.Driver",
                "jdbc:postgresql://postgres:5432/postgres",
                ImmutableMap.of(
                        "username", "postgres",
                        "password", "crmapi"
                )
        );

        DSLContext dslContext = DSL.using(this.database.getConnection());

        dslContext.transaction(configuration -> {
            this.userRepository = new UserRepositoryImpl();
            ((UserRepositoryImpl) this.userRepository).setConfiguration(configuration);
        });

        Evolutions.applyEvolutions(database);

        setUpFixture();
    }

    private void setUpFixture() {
        User user = new User();
        user.setName("Jerome");
        user.setSurname("Samson");
        userRepository.addUser(user).ifPresent(
                userCreatedUUID -> this.userCreated = userRepository.getUser(userCreatedUUID)
        );
    }

    @Test
    public void shouldCreateANewUser() {
        assertTrue(userCreated.isPresent());
    }

    @Test
    public void shouldEditUser() {
        userCreated.ifPresent(userCreated -> {
            UserRequest user = new UserRequest("JR", "S");
            userRepository.editUser(userCreated.getUuid(), user);
            UserResponse userEdited = userRepository.getUser(userCreated.getUuid()).get();
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
        userRepository.addUser(user);
    }

    @Test
    public void shouldDeleteUser() {
        userCreated.ifPresent(userCreated -> {
            userRepository.deleteUser(userCreated.getUuid());
            Optional<UserResponse> user = userRepository.getUser(userCreated.getUuid());
            assertFalse(user.isPresent());
        });
    }

    @Test
    public void shouldGetOnlyThoseUsersActive() {
        userCreated.ifPresent(userCreated -> {
            userRepository.deleteUser(userCreated.getUuid());
            User newUser = new User();
            newUser.setName("JR");
            newUser.setSurname("SAM");
            UUID newUserUUID = userRepository.addUser(newUser).get();
            List<UserResponse> users = userRepository.getUsersActive();

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
