package unit.services;

import model.entities.CustomerRequest;
import model.pojos.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import repositories.CustomerRepository;
import repositories.RepositoryFactory;
import services.CustomerService;
import services.UploadService;
import services.impl.CustomerServiceImpl;

import java.io.File;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private static final String imageFileName = "photoname.jpg";

    private CustomerService customerService;
    private UploadService uploadService;
    private CustomerRepository customerRepository;
    private CustomerRequest customerRequest;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    public CustomerServiceTest() {
        customerRepository = mock(CustomerRepository.class);
        uploadService = mock(UploadService.class);
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        when(repositoryFactory.getCustomerRepository()).thenReturn(customerRepository);
        customerService = new CustomerServiceImpl(repositoryFactory, uploadService);
        customerService.setCurrentUserId(1L);
    }

    @Before
    public void setUpFixture() {
        customerRequest = new CustomerRequest("Jerome", "Samson", imageFileName);
        File file = mock(File.class);
        when(uploadService.moveFileToImagesFolder(imageFileName)).thenReturn(file);
        when(file.getPath()).thenReturn(imageFileName);
    }

    private void verifyCustomerData() {
        Customer customer = customerArgumentCaptor.getValue();
        assertEquals("Jerome", customer.getName());
        assertEquals("Samson", customer.getSurname());
        assertEquals(1L, customer.getCreatedBy(), 0);
        assertEquals(1L, customer.getModifiedBy(), 0);
        assertEquals(imageFileName, customer.getPhotoUrl());
    }

    @Test
    public void shouldAddCustomer() {
        customerService.addCustomer(customerRequest);
        verify(customerRepository).addCustomer(customerArgumentCaptor.capture());
        verifyCustomerData();
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


}
