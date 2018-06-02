package model.entities;

import org.junit.Test;
import util.Notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRequestTest {

    @Test
    public void shouldNotifyAnErrorIfSurnameIsEmpty() {
        UserRequest userRequest = new UserRequest("Jerome", "");
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.SURNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfNameIsEmpty() {
        UserRequest userRequest = new UserRequest("", "Samson");
        Notification notification = userRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), UserRequest.NAME_REQUIRED_MESSAGE);
    }
    @Test
    public void shouldNotifyAnErrorIfNameAndSurnameAreEmpty() {
        UserRequest userRequest = new UserRequest("", "");
        Notification notification = userRequest.validation();
        String errorMessageExpected = UserRequest.NAME_REQUIRED_MESSAGE + "\n" + UserRequest.SURNAME_REQUIRED_MESSAGE;
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), errorMessageExpected);
    }
}
