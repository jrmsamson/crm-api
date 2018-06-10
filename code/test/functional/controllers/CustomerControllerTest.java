package functional.controllers;


import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.entities.responses.AddCustomerResponse;
import model.entities.requests.CustomerRequest;
import org.junit.Test;
import play.libs.Json;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.util.ByteString;
import play.libs.Files;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

import java.util.UUID;

import static play.mvc.Http.HttpVerbs.GET;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.HttpVerbs.PUT;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.DELETE;

public class CustomerControllerTest extends BaseControllerTest {

    private final String BASE_URL;
    private CustomerRequest customerRequest;

    public CustomerControllerTest() {
        BASE_URL = controllers.routes.CustomerController.getCustomersActive().url();
    }

    public void setUpAddCustomerFixture() {
        customerRequest = new CustomerRequest("Jerome", "Samson");
        buildRequest(POST, BASE_URL)
                .bodyJson(Json.toJson(customerRequest));
        makeRequest();
    }

    @Test
    public void shouldBeAvailableAddCustomer() {
        setUpAddCustomerFixture();
        assertEquals(CREATED, result.status());
    }

    @Test
    public void shouldGetCustomersActive() {
        buildRequest(GET, BASE_URL);
        makeRequest();
        assertEquals(OK, result.status());
    }

    @Test()
    public void shouldBeAvailableEditCustomer() {
        buildRequest(PUT, BASE_URL + "/" + UUID.randomUUID())
                .bodyJson(Json.toJson(new CustomerRequest("", "")));
        makeRequest();
        assertEquals(OK, result.status());
    }

    @Test()
    public void shouldBeAvailableDeleteCustomer() {
        buildRequest(DELETE, BASE_URL + "/" + UUID.randomUUID());
        makeRequest();
        assertEquals(OK, result.status());
    }

    @Test()
    public void shouldBeAvailableGetUserByUuid() {
        buildRequest(GET, BASE_URL + "/" + UUID.randomUUID());
        makeRequest();
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void shouldBeAvailableUpdateCustomerPhoto() throws IOException {
        setUpAddCustomerFixture();
        AddCustomerResponse addCustomerResponse = getAddCustomerResponseFromResult();

        Files.TemporaryFileCreator temporaryFileCreator = app.injector().instanceOf(Files.TemporaryFileCreator.class);
        Materializer materializer = app.injector().instanceOf(Materializer.class);

        File file = new File(getClass().getResource("/photo_test.png").getPath());

        Source<ByteString, CompletionStage<IOResult>> source = FileIO.fromPath(file.toPath());
        Http.MultipartFormData.FilePart<Source<ByteString, ?>> part =
                new Http.MultipartFormData.FilePart<>("photo", "", "image/png", source);

        buildRequest(POST, controllers.routes.CustomerController.updateCustomerPhoto(
                addCustomerResponse.getCustomerUuid().toString()).url()

        ).bodyMultipart(singletonList(part), temporaryFileCreator, materializer);

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    private AddCustomerResponse getAddCustomerResponseFromResult() throws IOException {
        return Json.fromJson(
                new ObjectMapper().readTree(contentAsString(result)),
                AddCustomerResponse.class
        );
    }

}
