package unit.model.entities;


import model.entities.requests.CustomerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import util.Notification;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CustomerRequestTest {

    private static final String NAME = "Jerome";
    private static final String SURNAME = "Samson";

    @Test
    public void shouldNotifyAnErrorIfNameIsNull() {
        CustomerRequest customerRequest = new CustomerRequest(null, SURNAME);
        Notification notification = customerRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), CustomerRequest.NAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfNameIsEmpty() {
        CustomerRequest customerRequest = new CustomerRequest(null, SURNAME);
        Notification notification = customerRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), CustomerRequest.NAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfSurnameIsNull() {
        CustomerRequest customerRequest = new CustomerRequest(NAME, null);
        Notification notification = customerRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), CustomerRequest.SURNAME_REQUIRED_MESSAGE);
    }

    @Test
    public void shouldNotifyAnErrorIfSurnameIsEmpty() {
        CustomerRequest customerRequest = new CustomerRequest(NAME, "");
        Notification notification = customerRequest.validation();
        assertTrue(notification.hasErrors());
        assertEquals(notification.errorMessage(), CustomerRequest.SURNAME_REQUIRED_MESSAGE);
    }

}
