package unit.model.entities;

import enums.Role;
import model.entities.AddUserRequest;
import org.junit.Test;
import util.Notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddUserRequestTest {

    @Test
    public void shouldNotifyAnErrorIfSurnameIsEmpty() {
        AddUserRequest addUserRequest = new AddUserRequest("Jerome", "", Role.USER, null, null);
        Notification notification = addUserRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), AddUserRequest.SURNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfNameIsEmpty() {
        AddUserRequest addUserRequest = new AddUserRequest("", "Samson", Role.USER, null, null);
        Notification notification = addUserRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), AddUserRequest.NAME_REQUIRED_MESSAGE);
    }
    @Test
    public void shouldNotifyAnErrorIfNameAndSurnameAreEmpty() {
        AddUserRequest addUserRequest = new AddUserRequest("", "", Role.USER, null, null);
        Notification notification = addUserRequest.validation();
        String errorMessageExpected = AddUserRequest.NAME_REQUIRED_MESSAGE + "\n" + AddUserRequest.SURNAME_REQUIRED_MESSAGE;
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), errorMessageExpected);
    }
}
