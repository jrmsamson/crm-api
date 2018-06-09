package services.impl;

import model.entities.CustomerRequest;
import model.pojos.Customer;
import repositories.RepositoryFactory;
import services.CustomerService;
import services.ImageService;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService {

    private final ImageService imageService;

    @Inject
    public CustomerServiceImpl(RepositoryFactory repositoryFactory, ImageService imageService) {
        // For testing purpose
        super(repositoryFactory);
        this.imageService = imageService;
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

    @Override
    public void deleteUser(UUID customerUUID) {
        repositoryFactory
                .getCustomerRepository()
                .deleteCustomer(customerUUID);
    }

    private Customer buildCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setCreatedBy(currentUserId);
        customer.setModifiedBy(currentUserId);

        File imageFile = imageService.moveImageFromTmpToImagesFolder(customerRequest.getPhotoFileName());
        customer.setPhotoUrl(imageFile.getPath());

        return customer;
    }


}
