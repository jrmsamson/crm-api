package unit.services;

import exceptions.CustomerDoesNotExistException;
import exceptions.ImageExtensionNotSupportedException;
import model.entities.requests.CustomerRequest;
import model.entities.responses.CustomerResponse;
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
    private Customer customerCaptor;

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

    private void verifyCustomerData() {
        customerCaptor = customerArgumentCaptor.getValue();
        assertEquals("Jerome", customerCaptor.getName());
        assertEquals("Samson", customerCaptor.getSurname());
        assertEquals(1L, customerCaptor.getCreatedBy(), 0);
        assertEquals(1L, customerCaptor.getModifiedBy(), 0);
    }

    @Test
    public void shouldAddCustomer() {
        customerService.addCustomer(customerRequest);
        verify(customerRepository).addCustomer(customerArgumentCaptor.capture());
        verifyCustomerData();
        assertNull(customerCaptor.getId());
        assertNull(customerCaptor.getPhotoName());
        assertNull(customerCaptor.getUuid());
    }

    @Test
    public void shouldEditCustomer() {
        UUID customerUUID = UUID.randomUUID();
        customerService.editCustomer(customerUUID, customerRequest);
        verify(customerRepository).editCustomer(
                customerArgumentCaptor.capture()
        );
        verifyCustomerData();
        assertEquals(customerUUID, customerArgumentCaptor.getValue().getUuid());
    }

    @Test
    public void shouldDeleteCustomer() {
        UUID customerUUID = UUID.randomUUID();
        customerService.deleteCustomer(customerUUID);
        verify(customerRepository).deleteCustomer(customerUUID);
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
        File file = new File("anyfile");
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
