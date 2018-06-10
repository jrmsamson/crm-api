package unit.model.entities;

import enums.Role;
import model.entities.UserRequest;
import org.junit.Test;
import util.Notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRequestTest {

    @Test
    public void shouldNotifyAnErrorIfSurnameIsEmpty() {
        UserRequest userRequest = new UserRequest("Jerome", "", Role.USER, null, null);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.SURNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfNameIsEmpty() {
        UserRequest userRequest = new UserRequest("", "Samson", Role.USER, null, null);
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.NAME_REQUIRED_MESSAGE);
    }
    @Test
    public void shouldNotifyAnErrorIfNameAndSurnameAreEmpty() {
        UserRequest userRequest = new UserRequest("", "", Role.USER, null, null);
        Notification notification = userRequest.validation();
        String errorMessageExpected = UserRequest.NAME_REQUIRED_MESSAGE + "\n" + UserRequest.SURNAME_REQUIRED_MESSAGE;
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), errorMessageExpected);
    }
}
