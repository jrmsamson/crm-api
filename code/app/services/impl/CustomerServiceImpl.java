package services.impl;

import exceptions.CustomerDoesNotExistException;
import exceptions.ImageExtensionNotSupportedException;
import exceptions.PhotoDoesNotExistException;
import exceptions.ImageUploadException;
import model.entities.responses.AddCustomerResponse;
import model.entities.requests.CustomerRequest;
import model.entities.responses.CustomerResponse;
import model.entities.requests.UpdateCustomerPhotoRequest;
import model.pojos.Customer;
import play.Application;
import repositories.RepositoryFactory;
import services.CustomerService;
import util.ConfigPath;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import static util.Constants.IMAGE_CONTENT_TYPE_EXTENSIONS;

public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService {

    private final String IMAGES_PATH;

    @Inject
    public CustomerServiceImpl(RepositoryFactory repositoryFactory,
                               Application application) {
        // For testing purpose
        super(repositoryFactory);
        IMAGES_PATH = application.config().getString(ConfigPath.IMAGES_PATH_CONFIG);
    }

    public AddCustomerResponse addCustomer(CustomerRequest customerRequest) {
        return new AddCustomerResponse(
                repositoryFactory
                        .getCustomerRepository()
                        .addCustomer(buildAddCustomer(customerRequest))
        );
    }

    private Customer buildAddCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setCreatedBy(currentUserId);
        customer.setModifiedBy(currentUserId);
        return customer;
    }

    public void editCustomer(UUID customerUUID, CustomerRequest customerRequest) {
        Customer customer = buildEditCustomer(customerRequest);
        customer.setUuid(customerUUID);
        repositoryFactory
                .getCustomerRepository()
                .editCustomerByUuid(customer);
    }

    private Customer buildEditCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setModifiedBy(currentUserId);
        return customer;
    }

    public void deleteCustomer(UUID customerUUID) {
        repositoryFactory
                .getCustomerRepository()
                .deleteCustomerUuid(customerUUID);
    }

    public List<CustomerResponse> getCustomersActive() {
        return repositoryFactory
                .getCustomerRepository()
                .getCustomersActive();
    }

    public String updateCustomerPhoto(UpdateCustomerPhotoRequest updateCustomerPhotoRequest) {
        validateIfCustomerExist(updateCustomerPhotoRequest.getCustomerUuuid());
        updateCustomerPhotoRequest.validateImage();
        checkImageContentType(updateCustomerPhotoRequest.getContentType());
        String photoName = saveCustomerPhoto(updateCustomerPhotoRequest);
        updateCustomerPhotoName(updateCustomerPhotoRequest, photoName);
        return photoName;
    }

    private void validateIfCustomerExist(UUID customerUuuid) {
        getCustomerByUuid(customerUuuid);
    }

    private void checkImageContentType(String contentType) {
        if (!IMAGE_CONTENT_TYPE_EXTENSIONS.containsKey(contentType))
            throw new ImageExtensionNotSupportedException();
    }

    private String saveCustomerPhoto(UpdateCustomerPhotoRequest updateCustomerPhotoRequest) {
        try {
            File file = new File(generateImageFileName(updateCustomerPhotoRequest));
            Files.move(updateCustomerPhotoRequest.getImageFile().toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return file.getName();
        } catch (IOException e) {
            throw new ImageUploadException(e);
        }
    }

    private void updateCustomerPhotoName(UpdateCustomerPhotoRequest updateCustomerPhotoRequest, String fileName) {
        Customer customer = new Customer();
        customer.setUuid(updateCustomerPhotoRequest.getCustomerUuuid());
        customer.setPhotoName(fileName);
        repositoryFactory
                .getCustomerRepository()
                .updateCustomerPhotoName(customer);
    }

    public File getCustomerImage(String imageName) {
        File customerImage = new File(IMAGES_PATH + imageName);

        if (!customerImage.exists())
            throw new PhotoDoesNotExistException();

        return customerImage;
    }

    public CustomerResponse getCustomerByUuid(UUID userUuid) {
        return repositoryFactory
                .getCustomerRepository()
                .getCustomerByUuid(userUuid)
                .orElseThrow(CustomerDoesNotExistException::new);
    }

    private String generateImageFileName(UpdateCustomerPhotoRequest updateCustomerPhotoRequest) {
        return IMAGES_PATH
                + updateCustomerPhotoRequest.getCustomerUuuid()
                + IMAGE_CONTENT_TYPE_EXTENSIONS.get(updateCustomerPhotoRequest.getContentType());
    }

}
