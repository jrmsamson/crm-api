package services;

import model.entities.CustomerRequest;
import model.entities.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService extends BaseService {

    void addCustomer(CustomerRequest customerRequest);

    void editCustomer(UUID customerUUID, CustomerRequest customerRequest);

    void deleteCustomer(UUID customerUUID);

    List<CustomerResponse> getCustomersActive();
}
