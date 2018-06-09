package services.impl;

import model.entities.CustomerRequest;
import model.entities.CustomerResponse;
import model.pojos.Customer;
import repositories.RepositoryFactory;
import services.CustomerService;
import services.UploadService;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.UUID;

public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService {

    private final UploadService uploadService;

    @Inject
    public CustomerServiceImpl(RepositoryFactory repositoryFactory, UploadService uploadService) {
        // For testing purpose
        super(repositoryFactory);
        this.uploadService = uploadService;
    }

    public void addCustomer(CustomerRequest customerRequest) {
        repositoryFactory
                .getCustomerRepository()
                .addCustomer(buildCustomer(customerRequest));
    }

    public void editCustomer(UUID customerUUID, CustomerRequest customerRequest) {
        Customer customer = buildCustomer(customerRequest);
        customer.setUuid(customerUUID);
        repositoryFactory
                .getCustomerRepository()
                .editCustomer(customer);
    }

    public void deleteCustomer(UUID customerUUID) {
        repositoryFactory
                .getCustomerRepository()
                .deleteCustomer(customerUUID);
    }

    public List<CustomerResponse> getCustomersActive() {
        return repositoryFactory
                .getCustomerRepository()
                .getCustomersActive();
    }

    private Customer buildCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setCreatedBy(currentUserId);
        customer.setModifiedBy(currentUserId);

        File imageFile = uploadService.moveFileToImagesFolder(customerRequest.getPhotoName());
        customer.setPhotoUrl(imageFile.getPath());

        return customer;
    }


}
