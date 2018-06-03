package unit.services;

import exceptions.UserRequestException;
import model.entities.UserRequest;
import model.entities.UserResponse;
import org.junit.Before;
import org.junit.Test;
import repositories.RepositoryFactory;
import repositories.UserRepository;
import services.UserService;
import services.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        RepositoryFactory repositoryFactoryMock = mock(RepositoryFactory.class);
        when(repositoryFactoryMock.getUserRepository()).thenReturn(userRepository);
        userService = new UserServiceImpl(repositoryFactoryMock);
    }

    @Test
    public void shouldCreateANewUser() {
        UserRequest userRequest = new UserRequest("Jerome", "Samson");
        UUID uuidMocked = UUID.randomUUID();
        when(userService.addUser(userRequest)).thenReturn(Optional.of(uuidMocked));
        UUID newUserUuid = userService.addUser(userRequest).get();
        assertEquals(uuidMocked, newUserUuid);
    }

    @Test(expected = UserRequestException.class)
    public void shouldThrowAnUserRequestExceptionWhenNameOrSurnameAreEmptyWhenItsGoingToCreateANewUser() {
        UserRequest userRequest = new UserRequest("Jerome", "");
        userService.addUser(userRequest);
    }

    @Test
    public void shouldEditAnUser() {
        UserRequest userRequest = new UserRequest("JRM", "SAM");
        UUID uuidMocked = UUID.randomUUID();
        userService.editUser(uuidMocked, userRequest);
        verify(userRepository).editUser(uuidMocked, userRequest);
    }

    @Test
    public void shouldDeleteAnUser() {
        UUID uuidMocked = UUID.randomUUID();
        userService.deleteUser(uuidMocked);
        verify(userRepository).deleteUser(uuidMocked);
    }

    @Test
    public void shouldGetAllUsersActive() {
        List<UserResponse> usersMocked = new ArrayList<>();
        usersMocked.add(new UserResponse("Jerome", "Samson", UUID.randomUUID()));
        usersMocked.add(new UserResponse("JRM", "SAM", UUID.randomUUID()));
        when(userRepository.getUsersActive()).thenReturn(usersMocked);

        List<UserResponse> users = userService.getUsersActive();

        for (int i = 0; i < usersMocked.size(); i++) {
            assertEquals(usersMocked.get(0), users.get(0));
        }
    }
}
