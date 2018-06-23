package unit.services;

import exceptions.CustomerDoesNotExistException;
import exceptions.CustomerRequestException;
import exceptions.CustomerWithSameNameAndSurnameAlreadyExistException;
import exceptions.ImageExtensionNotSupportedException;
import model.entities.requests.CustomerRequest;
import model.entities.responses.CustomerResponse;
import model.entities.requests.UpdateCustomerPhotoRequest;
import model.pojos.Customer;
import model.pojos.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
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
public class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private CustomerRequest customerRequest;

    private final String IMAGES_PATH;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    public CustomerServiceTest() {
        customerRepository = mock(CustomerRepository.class);
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        when(repositoryFactory.getCustomerRepository()).thenReturn(customerRepository);
        Application application = new GuiceApplicationBuilder().build();
        customerService = new CustomerServiceImpl(repositoryFactory, application);

        customerService.setCurrentUserId(1L);
        IMAGES_PATH = application.config().getString(ConfigPath.IMAGES_PATH_CONFIG);
    }

    @Before
    public void setUpFixture() {
        customerRequest = new CustomerRequest("Jerome", "Samson");
    }

    @Test
    public void shouldAddCustomer() {
        customerService.addCustomer(customerRequest);
        verify(customerRepository).addCustomer(customerArgumentCaptor.capture());
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
        String customerName = "Jerome";
        String customerSurname = "Samson";
        UUID customerUuid = UUID.randomUUID();
        when(customerRepository.getCustomerByNameAndSurname(customerName, customerSurname)).thenReturn(
                Optional.of(new CustomerResponse(customerUuid, customerName, customerSurname, null))
        );
        customerService.addCustomer(new CustomerRequest(customerName, customerSurname));
    }

    @Test
    public void shouldEditCustomer() {
        UUID customerUUID = UUID.randomUUID();
        customerService.editCustomer(customerUUID, customerRequest);
        verify(customerRepository).editCustomerByUuid(
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
    public void shouldDeleteCustomer() {
        UUID customerUUID = UUID.randomUUID();
        customerService.deleteCustomer(customerUUID);
        verify(customerRepository).deleteCustomerUuid(customerUUID);
    }

    @Test
    public void shouldGetCustomersActive() {
        customerService.getCustomersActive();
        verify(customerRepository).getCustomersActive();
    }

    @Test(expected = CustomerDoesNotExistException.class)
    public void shouldAllowToUploadPhotoForCustomersAlreadyExist() {
        when(customerRepository.getCustomerByUuid(any()))
                .thenReturn(Optional.empty());

        UpdateCustomerPhotoRequest updateCustomerPhotoRequest = new UpdateCustomerPhotoRequest(
                UUID.randomUUID(), new File(""), "image/png"
        );
        customerService.updateCustomerPhoto(updateCustomerPhotoRequest);
    }

    @Test
    public void shouldUpdateCustomerPhotoName() {
        UUID userUuid = UUID.randomUUID();
        when(customerRepository.getCustomerByUuid(any()))
                .thenReturn(Optional.of(new CustomerResponse(userUuid, "", "", "")));

        File file = new File(getClass().getResource("/photo_test.png").getPath());

        UpdateCustomerPhotoRequest updateCustomerPhotoRequest = new UpdateCustomerPhotoRequest(
                userUuid, file, "image/png"
        );

        String photoName = userUuid + ".png";

        customerService.updateCustomerPhoto(updateCustomerPhotoRequest);
        verify(customerRepository).updateCustomerPhotoName(customerArgumentCaptor.capture());
        Customer customer = customerArgumentCaptor.getValue();
        File newImageFile = new File(IMAGES_PATH + photoName);

        assertEquals(userUuid, customer.getUuid());
        assertEquals(photoName, customer.getPhotoName());
        assertTrue(newImageFile.exists());

        newImageFile.delete();
    }

    @Test(expected = ImageExtensionNotSupportedException.class)
    public void shouldAllowOnlyImageAsPhoto() throws IOException {
        File file = new File("/tmp/anyfile");
        file.createNewFile();

        UUID userUuid = UUID.randomUUID();

        when(customerRepository.getCustomerByUuid(any()))
                .thenReturn(Optional.of(new CustomerResponse(userUuid, "", "", "")));

        UpdateCustomerPhotoRequest updateCustomerPhotoRequest = new UpdateCustomerPhotoRequest(
                userUuid, file, "image/png"
        );
        customerService.updateCustomerPhoto(updateCustomerPhotoRequest);
    }

    @Test
    public void shouldGetCustomerByUuid() {
        UUID userUuid = UUID.randomUUID();
        CustomerResponse customerResponse = new CustomerResponse(UUID.randomUUID(), "", "", "");
        when(customerRepository.getCustomerByUuid(userUuid)).thenReturn(Optional.of(customerResponse));
        customerService.getCustomerByUuid(userUuid);
        verify(customerRepository).getCustomerByUuid(userUuid);
    }


}
