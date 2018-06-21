package unit.model.entities;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import enums.Role;
import model.entities.requests.UserRequest;
import org.junit.Test;
import play.Logger;
import util.Notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserRequestTest {

    private static final String NAME = "Jerome";
    private static final String SURNAME = "Samson";
    private static final Role ROLE = Role.USER;
    private static final String USERNAME = "myusername";
    private static final String PASSWORD = "mypassword";

    @Test
    public void shouldNotifyAnErrorIfNameIsNull() {
        UserRequest userRequest = new UserRequest(null, SURNAME, Role.USER, USERNAME, PASSWORD);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.NAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfNameIsEmpty() {
        UserRequest userRequest = new UserRequest("", SURNAME, Role.USER, USERNAME, PASSWORD);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.NAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfSurnameIsNull() {
        UserRequest userRequest = new UserRequest(NAME, null, Role.USER, USERNAME, PASSWORD);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.SURNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfSurnameIsEmpty() {
        UserRequest userRequest = new UserRequest(NAME, "", Role.USER, USERNAME, PASSWORD);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.SURNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfUsernameIsNull() {
        UserRequest userRequest = new UserRequest(NAME, SURNAME, Role.USER, null, PASSWORD);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.USERNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfRoleIsNull() {
        UserRequest userRequest = new UserRequest(NAME, SURNAME, null, USERNAME, PASSWORD);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.ROLE_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfUsernameIsEmpty() {
        UserRequest userRequest = new UserRequest(NAME, SURNAME, Role.USER, "", PASSWORD);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.USERNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfPasswordIsNull() {
        UserRequest userRequest = new UserRequest(NAME, SURNAME, Role.USER, USERNAME, null);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.PASSWORD_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfPasswordIsEmpty() {
        UserRequest userRequest = new UserRequest(NAME, SURNAME, Role.USER, USERNAME, "");
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.PASSWORD_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyMoreThanOneErrorIfMoreThanOneArgumentIsEmpty() {
        UserRequest userRequest = new UserRequest("", null, null, null, "");
        Notification notification = userRequest.validation();

        String errors = Joiner.on("\n").join(
                UserRequest.NAME_REQUIRED_MESSAGE,
                UserRequest.SURNAME_REQUIRED_MESSAGE,
                UserRequest.ROLE_REQUIRED_MESSAGE,
                UserRequest.USERNAME_REQUIRED_MESSAGE,
                UserRequest.PASSWORD_REQUIRED_MESSAGE
        );

        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), errors);
    }

}
