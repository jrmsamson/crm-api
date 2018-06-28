package unit.services;

import exceptions.CustomerDoesNotExistException;
import exceptions.CustomerRequestException;
import exceptions.CustomerWithSameNameAndSurnameAlreadyExistException;
import exceptions.ImageExtensionNotSupportedException;
import model.entities.requests.CustomerRequest;
import model.entities.requests.UpdateCustomerPhotoRequest;
import model.pojos.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import repositories.CustomerRepository;
import repositories.RepositoryFactory;
import services.CustomerService;
import services.impl.CustomerServiceImpl;
import util.ConfigPath;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest extends WithApplication {

    private final RepositoryFactory repositoryFactory;
    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private CustomerRequest customerRequest;

    private String IMAGES_PATH;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    public CustomerServiceTest() {
        customerRequest = new CustomerRequest("Jerome", "Samson");
        customerRepository = mock(CustomerRepository.class);
        repositoryFactory = mock(RepositoryFactory.class);
        when(repositoryFactory.getCustomerRepository()).thenReturn(customerRepository);
    }

    @Before
    public void setUp() {
        customerService = new CustomerServiceImpl(repositoryFactory, app);
        customerService.setCurrentUserId(1L);
        IMAGES_PATH = app.config().getString(ConfigPath.IMAGES_PATH_CONFIG);
    }

    @Test
    public void shouldAddCustomer() {
        customerService.addCustomer(customerRequest);
        verify(customerRepository).add(customerArgumentCaptor.capture());
        Customer customer = customerArgumentCaptor.getValue();

        assertEquals("Jerome", customer.getName());
        assertEquals("Samson", customer.getSurname());
        assertEquals(1L, customer.getCreatedBy(), 0);
        assertEquals(1L, customer.getModifiedBy(), 0);
        assertNull(customer.getId());
        assertNull(customer.getPhotoName());
        assertNull(customer.getUuid());
    }


    @Test(expected = CustomerRequestException.class)
    public void shouldThrowAnExceptionIfCustomerRequestIsNotValid() {
        CustomerRequest customerRequest = new CustomerRequest("", "");
        customerService.addCustomer(customerRequest);
    }

    @Test(expected = CustomerWithSameNameAndSurnameAlreadyExistException.class)
    public void shouldThrowAnExceptionWhenThereAlreadyExistAnCustomerWithTheSameNameAndSurname() {
        when(customerRepository.getByNameAndSurname(any(), any())).thenReturn(
                Optional.of(new Customer())
        );
        customerService.addCustomer(new CustomerRequest("Jerome", "Samson"));
    }

    @Test(expected = CustomerRequestException.class)
    public void shouldThrowAnExceptionIfCustomerRequestIsNotValidWhenItsGoingToUpdateTheCustomer() {
        CustomerRequest customerRequest = new CustomerRequest("", "");
        customerService.updateCustomer(UUID.randomUUID(), customerRequest);
    }

    @Test(expected = CustomerWithSameNameAndSurnameAlreadyExistException.class)
    public void shouldThrowAnExceptionWhenThereAlreadyExistAnCustomerWithTheSameNameAndSurnameWhenItsGoingToUpdateTheCustomer() {
        Customer customer = new Customer();
        customer.setUuid(UUID.randomUUID());
        when(customerRepository.getByNameAndSurname(any(), any())).thenReturn(
                Optional.of(customer)
        );
        customerService.updateCustomer(UUID.randomUUID(), new CustomerRequest("Jerome", "Samson"));
    }

    @Test()
    public void shouldAllowUpdateTheCustomerIfTheCustomerWithTheSameNameAndSurnameIsTheCurrentCustomerToBeEdited() {
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setUuid(uuid);
        when(customerRepository.getByNameAndSurname(any(), any())).thenReturn(
                Optional.of(customer)
        );
        customerService.updateCustomer(uuid, new CustomerRequest("Jerome", "Samson"));
    }

    @Test
    public void shouldUpdateCustomer() {
        UUID customerUUID = UUID.randomUUID();
        customerService.updateCustomer(customerUUID, customerRequest);
        verify(customerRepository).update(
                customerArgumentCaptor.capture()
        );
        Customer customer = customerArgumentCaptor.getValue();
        assertEquals("Jerome", customer.getName());
        assertEquals("Samson", customer.getSurname());
        assertEquals(1L, customer.getModifiedBy(), 0);
        assertEquals(customerUUID, customerArgumentCaptor.getValue().getUuid());
        assertNull(customer.getId());
        assertNull(customer.getCreatedBy());
        assertNull(customer.getPhotoName());
    }

    @Test
    public void shouldDeleteCustomerByUuid() {
        UUID customerUUID = UUID.randomUUID();
        customerService.deleteCustomerByUuid(customerUUID);
        verify(customerRepository).deleteByUuid(customerUUID);
    }

    @Test
    public void shouldGetCustomersActive() {
        customerService.getCustomersActive();
        verify(customerRepository).getActive();
    }

    @Test(expected = CustomerDoesNotExistException.class)
    public void shouldAllowToUploadPhotoForCustomersAlreadyExist() {
        when(customerRepository.getByUuid(any()))
                .thenReturn(Optional.empty());

        UpdateCustomerPhotoRequest updateCustomerPhotoRequest = new UpdateCustomerPhotoRequest(
                UUID.randomUUID(), new File(""), "image/png"
        );
        customerService.updateCustomerPhoto(updateCustomerPhotoRequest);
    }

    @Test
    public void shouldUpdateCustomerPhotoName() {
        UUID userUuid = UUID.randomUUID();
        when(customerRepository.getByUuid(any()))
                .thenReturn(Optional.of(new Customer()));

        File file = new File(getClass().getResource("/photo_test.png").getPath());

        UpdateCustomerPhotoRequest updateCustomerPhotoRequest = new UpdateCustomerPhotoRequest(
                userUuid, file, "image/png"
        );

        String photoName = userUuid + ".png";

        customerService.updateCustomerPhoto(updateCustomerPhotoRequest);
        verify(customerRepository).update(customerArgumentCaptor.capture());

        Customer customer = customerArgumentCaptor.getValue();
        File newImageFile = new File(IMAGES_PATH + photoName);

        assertEquals(photoName, customer.getPhotoName());
        assertTrue(newImageFile.exists());

        newImageFile.delete();
    }

    @Test(expected = ImageExtensionNotSupportedException.class)
    public void shouldAllowOnlyImageAsPhoto() throws IOException {
        File file = new File("/tmp/anyfile");
        file.createNewFile();

        UUID userUuid = UUID.randomUUID();

        when(customerRepository.getByUuid(any()))
                .thenReturn(Optional.of(new Customer()));

        UpdateCustomerPhotoRequest updateCustomerPhotoRequest = new UpdateCustomerPhotoRequest(
                userUuid, file, "image/png"
        );
        customerService.updateCustomerPhoto(updateCustomerPhotoRequest);
    }

    @Test
    public void shouldGetCustomerByUuid() {
        UUID userUuid = UUID.randomUUID();
        when(customerRepository.getByUuid(userUuid)).thenReturn(Optional.of(new Customer()));
        customerService.getCustomerByUuid(userUuid);
        verify(customerRepository).getByUuid(userUuid);
    }


}
