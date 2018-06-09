package functional.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.entities.CustomerRequest;
import model.entities.CustomerResponse;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import util.ConfigPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.GET;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;

public class CustomerControllerTest extends BaseControllerTest {

    public static final String IMAGE_FILE_NAME = "myimage.jpg";
    private final String TMP_PATH;
    private final String IMAGE_PATH;
    private CustomerRequest customerRequest;

    public CustomerControllerTest() {
        TMP_PATH = app.config().getString(ConfigPath.TMP_PATH_CONFIG);
        IMAGE_PATH = app.config().getString(ConfigPath.IMAGES_PATH_CONFIG);
    }

    @Test
    public void shouldBeAvailableAddCustomer() throws IOException {
        Path path = Files.createTempFile("", "");
        Logger.info(path.toString());
        customerRequest = new CustomerRequest("Jerome", "Samson", IMAGE_FILE_NAME);
        buildRequest(POST, "/customers")
                .bodyJson(Json.toJson(customerRequest));
        makeRequest();
        assertEquals(OK, makeRequest().status());
    }

    @Test
    public void shouldGetCustomersActive() throws IOException {
        buildRequest(GET, "/customers");
        List<CustomerResponse> customerResponses = new ObjectMapper()
                .readValue(contentAsString(makeRequest()), new TypeReference<List<CustomerResponse>>(){});

        assertEquals(1, customerResponses.size());
    }

    @Test
    public void shouldBeAvailableEditCustomer() {
    }

}
