package unit.services;

import enums.Role;
import exceptions.UserRequestException;
import model.entities.AddLoginRequest;
import model.entities.AddUserRequest;
import model.entities.UserResponse;
import model.pojos.User;
import org.junit.Before;
import org.junit.Test;
import repositories.RepositoryFactory;
import repositories.UserRepository;
import services.LoginService;
import services.RoleService;
import services.UserService;
import services.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepositoryMock;
    private RoleService roleServiceMock;

    @Before
    public void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        roleServiceMock = mock(RoleService.class);
        RepositoryFactory repositoryFactoryMock = mock(RepositoryFactory.class);
        when(repositoryFactoryMock.getUserRepository()).thenReturn(userRepositoryMock);
        userService = new UserServiceImpl(repositoryFactoryMock, roleServiceMock);
        setUpFixture();
    }

    private void setUpFixture() {
        when(userRepositoryMock.addUser(new User())).thenReturn(0L);
        AddUserRequest addUserRequest = new AddUserRequest("Jerome", "Samson", Role.USER, "jer0Me", "password");
        userService.addUser(addUserRequest);
    }

    @Test
    public void shouldCreateANewUserWithLogin() {
        verify(roleServiceMock).getRoleId(Role.USER);
        // TODO
//        verify(loginServiceMock).addLoginForUser(0L, new AddLoginRequest("jer0Me", "password"));
    }

    @Test(expected = UserRequestException.class)
    public void shouldThrowAnUserRequestExceptionWhenNameOrSurnameAreEmptyWhenItsGoingToCreateANewUser() {
        AddUserRequest addUserRequest = new AddUserRequest("Jerome", "", Role.USER, "jer0Me", "password");
        userService.addUser(addUserRequest);
    }

    @Test
    public void shouldEditAnUser() {
        AddUserRequest addUserRequest = new AddUserRequest("JRM", "SAM", Role.USER, "jer0Me", "password");
        UUID uuidMocked = UUID.randomUUID();
        userService.editUser(uuidMocked, addUserRequest);
        verify(userRepositoryMock).editUser(uuidMocked, addUserRequest);
    }

    @Test
    public void shouldDeleteAnUser() {
        UUID uuidMocked = UUID.randomUUID();
        userService.deleteUser(uuidMocked);
        verify(userRepositoryMock).deleteUser(uuidMocked);
    }

    @Test
    public void shouldGetAllUsersActive() {
        List<UserResponse> usersMocked = new ArrayList<>();
        usersMocked.add(new UserResponse("Jerome", "Samson", UUID.randomUUID(), Role.USER));
        usersMocked.add(new UserResponse("JRM", "SAM", UUID.randomUUID(), Role.USER));
        when(userRepositoryMock.getUsersActive()).thenReturn(usersMocked);

        List<UserResponse> users = userService.getUsersActive();

        for (int i = 0; i < usersMocked.size(); i++) {
            assertEquals(usersMocked.get(0), users.get(0));
        }
    }

}
