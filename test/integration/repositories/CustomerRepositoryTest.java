package integration.repositories;

import exceptions.CustomerRepositoryException;
import exceptions.CustomerWithSameNameAndSurnameAlreadyExistException;
import model.entities.responses.CustomerResponse;
import model.pojos.Customer;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import repositories.impl.CustomerRepositoryImpl;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class CustomerRepositoryTest {

    private static final String NAME = "Jerome";
    private static final String SURNAME = "Samson";

    private CustomerRepositoryImpl customerRepository;
    private Database database;
    private Customer lastCustomerCreated;

    public CustomerRepositoryTest() {
        Application application = new GuiceApplicationBuilder().build();
        database = application.injector().instanceOf(Database.class);
        customerRepository = new CustomerRepositoryImpl();
        DSLContext dslContext = DSL.using(this.database.getConnection());
        customerRepository.setDslContext(dslContext);
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
        setUpFixture();
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }

    private void setUpFixture() {
        Customer customer = new Customer();
        customer.setName(NAME);
        customer.setSurname(SURNAME);
        customer.setCreatedBy(1L);
        customer.setModifiedBy(1L);
        customerRepository.add(customer);
        List<Customer> customers = customerRepository.getActive();
        lastCustomerCreated = customers.get(customers.size() - 1);
    }

    @Test
    public void shouldAddCustomer() {
        assertEquals("Jerome", lastCustomerCreated.getName());
        assertEquals("Samson", lastCustomerCreated.getSurname());
    }

    @Test(expected = CustomerRepositoryException.class)
    public void shouldThrowAnExceptionWhenNewUserNameIsNull() {
        Customer customer = new Customer();
        customer.setSurname("Samson");
        customerRepository.add(customer);
    }

    @Test(expected = CustomerRepositoryException.class)
    public void shouldThrowAnExceptionWhenNewUserSurnameIsNull() {
        Customer customer = new Customer();
        customer.setName("Jerome");
        customerRepository.add(customer);
    }

    @Test(expected = CustomerRepositoryException.class)
    public void shouldThrowAnExceptionWhenTheCustomerToBeAddedAlreadyExist() {
        setUpFixture();
    }

    @Test
    public void shouldEditCustomer() {
        Customer customer = new Customer();
        customer.setName("JRM");
        customer.setSurname("SAM");
        customer.setUuid(lastCustomerCreated.getUuid());
        customer.setModifiedBy(1L);
        customer.setPhotoName("myphoto");
        customerRepository.update(customer);
        Customer customerEdited =
                customerRepository.getByUuid(lastCustomerCreated.getUuid()).get();
        assertEquals("JRM", customerEdited.getName());
        assertEquals("SAM", customerEdited.getSurname());
        assertEquals("myphoto", customerEdited.getPhotoName());
    }

    @Test
    public void shouldDeleteCustomer() {
        customerRepository.deleteByUuid(lastCustomerCreated.getUuid());
        List<Customer> customers = customerRepository.getActive();
        assertEquals(0, customers.size());
    }

    @Test
    public void shouldReturnCustomerGottenByNameAndSurname() {
        Customer user = new Customer();
        user.setSurname(lastCustomerCreated.getSurname());
        assertTrue(customerRepository.getByNameAndSurname(
                lastCustomerCreated.getName(), lastCustomerCreated.getSurname()
        ).isPresent());
    }

}
