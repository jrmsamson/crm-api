package repositories;

import model.entities.CustomerResponse;
import model.pojos.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends BaseRepository {

    Optional<CustomerResponse> getCustomerByUuid(UUID uuid);

    List<CustomerResponse> getAllCustomerActive();

    void addCustomer(Customer customer);

    void editCustomer(Customer customer);

    void deleteCustomer(UUID uuid);

    void updateCustomerPhotoName(Customer customer);
}
