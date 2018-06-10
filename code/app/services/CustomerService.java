package services;

import model.entities.AddCustomerResponse;
import model.entities.CustomerRequest;
import model.entities.CustomerResponse;
import model.entities.UpdateCustomerPhotoRequest;

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
