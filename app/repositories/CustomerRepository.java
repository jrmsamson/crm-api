package repositories;

import model.pojos.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends BaseRepository {

    Optional<Customer> getByUuid(UUID uuid);

    List<Customer> getActive();

    UUID add(Customer customer);

    void update(Customer customer);

    void deleteByUuid(UUID uuid);

    Optional<Customer> getByNameAndSurname(String name, String surname);
}
