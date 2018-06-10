package unit.services;

import enums.Role;
import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import model.entities.requests.UserRequest;
import model.entities.responses.UserResponse;
import model.pojos.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import play.Logger;
import repositories.RepositoryFactory;
import repositories.RoleRepository;
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

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepositoryMock;
    private RoleRepository roleRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Before
    public void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        RepositoryFactory repositoryFactoryMock = mock(RepositoryFactory.class);
        when(repositoryFactoryMock.getUserRepository()).thenReturn(userRepositoryMock);
        when(repositoryFactoryMock.getRoleRepository()).thenReturn(roleRepository);
        userService = new UserServiceImpl(repositoryFactoryMock);
        userService.setCurrentUserId(1L);
        setUpFixture();
    }

    private void setUpFixture() {
        when(roleRepository.getRoleId(Role.USER)).thenReturn(Optional.of(2));
        UserRequest userRequest = new UserRequest("Jerome", "Samson", Role.USER, "jer0Me", "password");
        userService.addUser(userRequest);
    }

    @Test
    public void shouldCreateANewUser() {
        verify(userRepositoryMock).addUser(userCaptor.capture());
        User user = userCaptor.getValue();
        assertEquals("Jerome", user.getName());
        assertEquals("Samson", user.getSurname());
        assertEquals(2, user.getRoleId(), 0);
    }

    @Test(expected = UserRequestException.class)
    public void shouldThrowAnUserRequestExceptionWhenNameOrSurnameAreEmptyWhenItsGoingToCreateANewUser() {
        UserRequest userRequest = new UserRequest("Jerome", "", Role.USER, null, null);
        userService.addUser(userRequest);
    }

    @Test
    public void shouldEditAnUser() {
        UserRequest userRequest = new UserRequest("JRM", "SAM", Role.USER, null, null);
        UUID uuidMocked = UUID.randomUUID();
        userService.editUser(uuidMocked, userRequest);
        verify(userRepositoryMock).editUserByUuid(userCaptor.capture());
        User user = userCaptor.getValue();
        assertEquals("JRM", user.getName());
        assertEquals("SAM", user.getSurname());
        assertEquals(2, user.getRoleId(), 0);
    }

    @Test
    public void shouldDeleteAnUser() {
        UUID uuidMocked = UUID.randomUUID();
        userService.deleteUser(uuidMocked);
        verify(userRepositoryMock).deleteUserByUuid(uuidMocked);
    }

    @Test
    public void shouldGetAllUsersActive() {
        List<UserResponse> usersMocked = new ArrayList<>();
        usersMocked.add(new UserResponse("Jerome", "Samson", UUID.randomUUID(), Role.USER));
        usersMocked.add(new UserResponse("JRM", "SAM", UUID.randomUUID(), Role.USER));
        when(userRepositoryMock.getUsersActive(1L)).thenReturn(usersMocked);

        List<UserResponse> users = userService.getUsersActive();

        for (int i = 0; i < usersMocked.size(); i++) {
            assertEquals(usersMocked.get(0), users.get(0));
        }
    }

    @Test
    public void shouldGetUserIdByUuid() {
        UUID userUuid = UUID.randomUUID();
        when(userRepositoryMock.getUserIdByUuid(userUuid)).thenReturn(Optional.of(1L));
        userService.getUserIdByUuid(userUuid);
        verify(userRepositoryMock).getUserIdByUuid(userUuid);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void shouldThrowAnExceptionIfUserIdDoesNotExist() {
        userService.getUserIdByUuid(UUID.randomUUID());
    }

    @Test
    public void shouldGetUserByUuid() {
        UUID userUuid = UUID.randomUUID();
        when(userRepositoryMock.getUserByUuid(userUuid)).thenReturn(
                Optional.of(new UserResponse("", "",null, null))
        );
        userService.getUserByUuid(userUuid);
        verify(userRepositoryMock).getUserByUuid(userUuid);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void shouldThrowAnExceptionIfDoesNotExistTheUserWhenItsGoingToFindHimByUuid() {
        UUID userUuid = UUID.randomUUID();
        when(userRepositoryMock.getUserByUuid(userUuid)).thenReturn(
                Optional.empty()
        );
        userService.getUserByUuid(userUuid);
    }

}
