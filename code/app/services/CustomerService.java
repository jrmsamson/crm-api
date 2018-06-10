package services;

import model.entities.responses.AddCustomerResponse;
import model.entities.requests.CustomerRequest;
import model.entities.responses.CustomerResponse;
import model.entities.requests.UpdateCustomerPhotoRequest;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface CustomerService extends BaseService {

    AddCustomerResponse addCustomer(CustomerRequest customerRequest);

    void editCustomer(UUID customerUUID, CustomerRequest customerRequest);

    void deleteCustomer(UUID customerUUID);

    List<CustomerResponse> getCustomersActive();

    String updateCustomerPhoto(UpdateCustomerPhotoRequest updateCustomerPhotoRequest);

    File getCustomerImage(String imageName);

    CustomerResponse getCustomerByUuid(UUID userUuid);
}
