package controllers;

import akka.stream.IOResult;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import model.entities.CustomerRequest;
import model.entities.CustomerResponse;
import model.entities.UpdateCustomerPhotoRequest;
import play.http.HttpEntity;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.ResponseHeader;
import play.mvc.Result;
import services.CustomerService;
import util.annotation.Secured;
import util.annotation.Transactional;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static util.Constants.PHOTO_FILE_PART_NAME;

@Secured
@Transactional
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    private HttpExecutionContext ec;

    @Inject
    public CustomerController(CustomerService customerService,
                              HttpExecutionContext ec) {

        this.ec = ec;
        this.customerService = customerService;
        init(customerService);
    }

    public CompletionStage<Result> getCustomersActive() {
        return CompletableFuture.supplyAsync(
                customerService::getCustomersActive
        ).thenApplyAsync(customers -> ok(
                Json.toJson(setCustomerImageUrls(customers))
        ), ec.current());
    }

    private List<CustomerResponse> setCustomerImageUrls(List<CustomerResponse> customers) {
        for(CustomerResponse customer : customers) {
            customer.setPhotoUrl(
                    controllers.routes.CustomerController
                            .getCustomerPhoto(customer.getPhotoUrl())
                            .absoluteURL(request())
            );
        }
        return customers;
    }

    public CompletionStage<Result> addCustomer() {
        CustomerRequest customerRequest = getCustomerRequestFromRequest();
        return CompletableFuture.supplyAsync(() ->
            customerService.addCustomer(customerRequest)
        ).thenApply(
                addCustomerResponse -> created(
                        Json.toJson(addCustomerResponse)
                )
        );
    }

    public CompletionStage<Result> editCustomer(String uuid) {
        CustomerRequest customerRequest = getCustomerRequestFromRequest();
        return CompletableFuture.runAsync(() ->
            customerService.editCustomer(
                    UUID.fromString(uuid),
                    customerRequest
            )
        ).thenApply(
                aVoid -> ok()
        );
    }

    public CompletionStage<Result> deleteCustomer(String uuid) {
        return CompletableFuture.runAsync(() ->
                customerService.deleteCustomer(UUID.fromString(uuid))
        ).thenApply(
                aVoid -> ok()
        );
    }

    public CompletionStage<Result> getCustomerByUuid(String uuid) {
        return CompletableFuture.supplyAsync(() ->
                customerService.getCustomerByUuid(UUID.fromString(uuid))
        ).thenApply(
                aVoid -> ok()
        );
    }

    public Result updateCustomerPhoto(String uuid) {
            Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart<File> filePart = formData.getFile(PHOTO_FILE_PART_NAME);
            String photoName = customerService.updateCustomerPhoto(
                    new UpdateCustomerPhotoRequest(
                            UUID.fromString(uuid),
                            filePart.getFile(),
                            filePart.getContentType()
                    )
            );
            return ok(
                controllers.routes
                        .CustomerController
                        .getCustomerPhoto(photoName)
                        .absoluteURL(request())
            );
    }

    public Result getCustomerPhoto(String imageName) {
        File file = customerService.getCustomerImage(imageName);
        Path path = file.toPath();
        Source<ByteString, CompletionStage<IOResult>> source = FileIO.fromPath(path);

        return new Result(
                new ResponseHeader(OK, Collections.emptyMap()),
                new HttpEntity.Streamed(source, Optional.empty(), Optional.empty())
        );
    }

    private CustomerRequest getCustomerRequestFromRequest() {
        return Json.fromJson(request().body().asJson(), CustomerRequest.class);
    }
}
