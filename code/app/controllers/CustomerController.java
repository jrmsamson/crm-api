package controllers;

import model.entities.CustomerRequest;
import play.libs.Json;
import play.mvc.Result;
import services.CustomerService;
import util.annotation.Secured;
import util.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Secured
@Transactional
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    @Inject
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
        init(customerService);
    }

    public CompletionStage<Result> getCustomersActive() {
        return CompletableFuture.supplyAsync(
                customerService::getCustomersActive
        ).thenApply(customers -> ok(
                Json.toJson(customers)
        ));
    }

    public CompletionStage<Result> addCustomer() {
        return CompletableFuture.runAsync(() ->
            customerService.addCustomer(getCustomerRequestFromRequest())
        ).thenApplyAsync(
                aVoid -> ok()
        );
    }

    public CompletionStage<Result> editCustomer(String uuid) {
        return CompletableFuture.runAsync(() ->
            customerService.editCustomer(
                    UUID.fromString(uuid),
                    getCustomerRequestFromRequest()
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

    private CustomerRequest getCustomerRequestFromRequest() {
        return Json.fromJson(request().body().asJson(), CustomerRequest.class);
    }
}
