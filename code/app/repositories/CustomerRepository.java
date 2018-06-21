package repositories;

import model.entities.responses.CustomerResponse;
import model.pojos.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends BaseRepository {

    Optional<CustomerResponse> getCustomerByUuid(UUID uuid);

    List<CustomerResponse> getCustomersActive();

    UUID addCustomer(Customer customer);

    void editCustomerByUuid(Customer customer);

    void deleteCustomerUuid(UUID uuid);

    void updateCustomerPhotoName(Customer customer);

    Optional<CustomerResponse> getCustomerByNameAndSurname(String name, String surname);
}
