package services;

import model.entities.CustomerRequest;

import java.util.UUID;

public interface CustomerService extends BaseService {

    void addCustomer(CustomerRequest customerRequest);

    void editCustomer(UUID customerUUID, CustomerRequest customerRequest);

    void deleteUser(UUID customerUUID);
}
