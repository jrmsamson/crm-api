package unit.services;

import exceptions.UserDoesNotExistException;
import exceptions.UserRequestException;
import exceptions.UserWithSameNameAndSurnameAlreadyExistException;
import model.entities.requests.UserRequest;
import model.entities.responses.UserToken;
import model.pojos.Role;
import model.pojos.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import repositories.RepositoryFactory;
import repositories.RoleRepository;
import repositories.UserRepository;
import services.UserService;
import services.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final Integer ADMIN_ROLE_ID = 1;
    private static final Integer USER_ROLE_ID = 1;
    private static final String USER_ROLE = enums.Role.USER.getName();

    private UserService userService;
    private UserRepository userRepository;
    private RoleRepository roleRepositoryMock;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepositoryMock = mock(RoleRepository.class);
        RepositoryFactory repositoryFactoryMock = mock(RepositoryFactory.class);
        when(repositoryFactoryMock.getUserRepository()).thenReturn(userRepository);
        when(repositoryFactoryMock.getRoleRepository()).thenReturn(roleRepositoryMock);

        Role role = new Role();
        role.setId(USER_ROLE_ID);
        role.setName(USER_ROLE);
        when(roleRepositoryMock.getById(any())).thenReturn(Optional.of(role));
        when(roleRepositoryMock.getByName(any())).thenReturn(Optional.of(role));
        userService = new UserServiceImpl(repositoryFactoryMock);
        userService.setCurrentUserId(1L);
        setUpFixture();
    }

    private void setUpFixture() {
        UserRequest userRequest = new UserRequest(
                "Jerome", "Samson", USER_ROLE, "jer0Me", "password"
        );
        userService.addUser(userRequest);
    }


    @Test
    public void shouldAddUser() {
        verify(userRepository).add(userCaptor.capture());
        User user = userCaptor.getValue();
        assertEquals("Jerome", user.getName());
        assertEquals("Samson", user.getSurname());
        assertEquals(USER_ROLE_ID, user.getRoleId(), 0);
    }

    @Test(expected = UserRequestException.class)
    public void shouldThrowAnUserRequestExceptionWhenNameOrSurnameAreEmptyWhenItsGoingToCreateANewUser() {
        UserRequest userRequest = new UserRequest("Jerome", "", USER_ROLE, null, null);
        userService.addUser(userRequest);
    }

    @Test(expected = UserWithSameNameAndSurnameAlreadyExistException.class)
    public void shouldThrowAnExceptionWhenThereAlreadyExistAnUserWithTheSameNameAndSurname() {
        when(userRepository.getByNameAndSurname("Jerome", "Samson")).thenReturn(
                Optional.of(new User())
        );
        userService.addUser(
                new UserRequest(
                        "Jerome", "Samson", USER_ROLE, "myusername", "mypassword"
                )
        );
    }

    @Test(expected = UserWithSameNameAndSurnameAlreadyExistException.class)
    public void shouldThrowAnExceptionWhenThereAlreadyExistAnUserWithTheSameNameAndSurnameWhenItsGoingToUpdateTheUser() {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        when(userRepository.getByNameAndSurname(any(), any())).thenReturn(
                Optional.of(user)
        );
        userService.updateUser(
                UUID.randomUUID(),
                new UserRequest(
                        "Jerome", "Samson", USER_ROLE, "jer0Me", "password"
                )
        );
    }

    @Test
    public void shouldAllowUpdateTheCustomerIfTheCustomerWithTheSameNameAndSurnameIsTheCurrentCustomerToBeEdited() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUuid(uuid);
        when(userRepository.getByUuid(uuid)).thenReturn(
                Optional.of(user)
        );
        userService.updateUser(
                uuid,
                new UserRequest(
                        "Jerome", "Samson", USER_ROLE, "jer0Me", "password"
                )
        );
    }

    @Test
    public void shouldEditAnUser() {
        UserRequest userRequest = new UserRequest(
                "JRM", "SAM", USER_ROLE, "myusername", "mypassword"
        );
        UUID uuid = UUID.randomUUID();

        User userAdded = new User();
        userAdded.setUuid(uuid);

        when(userRepository.getByUuid(uuid)).thenReturn(
                Optional.of(userAdded)
        );

        userService.updateUser(uuid, userRequest);
        verify(userRepository).update(userCaptor.capture());
        User user = userCaptor.getValue();
        assertEquals(uuid, user.getUuid());
        assertEquals("JRM", user.getName());
        assertEquals("SAM", user.getSurname());
        assertEquals(ADMIN_ROLE_ID, user.getRoleId());
    }

    @Test
    public void shouldDeleteAnUser() {
        UUID uuidMocked = UUID.randomUUID();
        userService.deleteUser(uuidMocked);
        verify(userRepository).deleteByUuid(uuidMocked);
    }

    @Test
    public void shouldGetAllUsersActive() {
        List<User> usersMocked = new ArrayList<>();
        usersMocked.add(new User());
        when(userRepository.getActive(1L)).thenReturn(usersMocked);
        assertEquals(1, userService.getUsersActive().size());
    }

    @Test
    public void shouldGetUserIdByUuid() {
        UUID userUuid = UUID.randomUUID();
        User user = new User();
        user.setId(2L);
        when(userRepository.getByUuid(userUuid)).thenReturn(Optional.of(user));
        assertEquals(2L, userService.getUserIdByUuid(userUuid), 0);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void shouldThrowAnExceptionIfUserIdDoesNotExist() {
        userService.getUserIdByUuid(UUID.randomUUID());
    }

    @Test
    public void shouldGetUserByUuid() {
        UUID userUuid = UUID.randomUUID();
        when(userRepository.getByUuid(userUuid)).thenReturn(
                Optional.of(new User())
        );
        userService.getUserByUuid(userUuid);
        verify(userRepository).getByUuid(userUuid);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void shouldReturnUserGottenByUserNameAndSurname() {
        UUID userUuid = UUID.randomUUID();
        when(userRepository.getByUuid(userUuid)).thenReturn(
                Optional.empty()
        );
        userService.getUserByUuid(userUuid);
    }

    @Test
    public void shouldReturnUserToken() {
        String token = "myToken";
        LocalDateTime tokenExpiration = LocalDateTime.now();
        User user = new User();
        user.setToken(token);
        user.setTokenExpiration(tokenExpiration);
        when(userRepository.getById(1L)).thenReturn(Optional.of(user));

        UserToken userToken = userService.getUserTokenByUserId(1L);
        assertEquals(token, userToken.getToken());
        assertEquals(tokenExpiration, userToken.getTokenExpiration());
    }

    @Test
    public void shouldRemoveCurrentUserToken() {
        String token = "myToken";
        LocalDateTime tokenExpiration = LocalDateTime.now();
        User user = new User();
        user.setToken(token);
        user.setTokenExpiration(tokenExpiration);
        when(userRepository.getById(1L)).thenReturn(Optional.of(user));
        userService.removeUserToken(1L);

        verify(userRepository).update(userCaptor.capture());
        User userToUpdate = userCaptor.getValue();
        assertNull(userToUpdate.getToken());
        assertNull(userToUpdate.getTokenExpiration());
    }

    @Test
    public void shouldRenewUserToken() {
        String token = "myToken";
        LocalDateTime tokenExpiration = LocalDateTime.now();
        User user = new User();
        user.setToken(token);
        user.setTokenExpiration(tokenExpiration);
        when(userRepository.getById(2L)).thenReturn(Optional.of(user));
        userService.renewUserToken(2L);

        verify(userRepository).update(userCaptor.capture());
        User userToUpdate = userCaptor.getValue();
        assertTrue(tokenExpiration.isBefore(userToUpdate.getTokenExpiration()));
    }

    @Test
    public void shouldSetUserToken() {
        User user = new User();
        when(userRepository.getById(1L)).thenReturn(Optional.of(user));
        userService.setUserTokenByUserId(1L);

        verify(userRepository).update(userCaptor.capture());

        User userUpdatedWithNewToken = userCaptor.getValue();
        assertNotNull(userUpdatedWithNewToken.getToken());
        assertNotNull(userUpdatedWithNewToken.getTokenExpiration());
    }

    @Test
    public void shouldGetUserRoleByUserId() {
        User user = new User();
        user.setRoleId(2);
        when(userRepository.getById(1L)).thenReturn(Optional.of(user));
        assertEquals(USER_ROLE, userService.getUserRoleByUserId(1L));
    }

}
