package functional.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import enums.Role;
import model.entities.responses.AddUserResponse;
import model.entities.requests.UserRequest;
import org.junit.Before;
import org.junit.Test;
import play.Logger;
import play.libs.Json;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class UserControllerTest extends BaseControllerTest {

    private final String BASE_URL;
    private UserRequest userRequest;

    public UserControllerTest() {
        BASE_URL = controllers.routes.UserController.getUsersActive().url();
    }

    @Before
    public void setUpAddUserFixture() {
        userRequest = new UserRequest(
                "Jerome", "Samson", Role.USER, "jer0Me", "password"
        );
        buildRequest(POST, BASE_URL)
                .bodyJson(Json.toJson(userRequest));
        makeRequest();
    }

    @Test
    public void shouldBeAvailableAddANewUser() {
        assertEquals(CREATED, result.status());
    }

    @Test
    public void shouldGetUserByUuid() throws IOException {
        AddUserResponse addUserResponse = getAddUserResponseFromResult();
        buildRequest(GET, BASE_URL + "/" + addUserResponse.getUserUuid());
        makeRequest();
        assertEquals(OK, result.status());
    }

    @Test
    public void shouldBeAvailableGetAllUsers() {
        buildRequest(GET, BASE_URL);
        makeRequest();
        assertEquals(OK, result.status());
    }

    @Test
    public void shouldBeAvailableEditUser() throws IOException {
        AddUserResponse addUserResponse = getAddUserResponseFromResult();
        userRequest = new UserRequest(
                "JRM", "SAM", Role.USER, null, null
        );
        buildRequest(PUT, BASE_URL + "/" + addUserResponse.getUserUuid())
                .bodyJson(Json.toJson(userRequest));
        makeRequest();
        assertEquals(OK, result.status());
    }

    @Test
    public void shouldBeAvailableDeleteUser() throws IOException {
        AddUserResponse addUserResponse = getAddUserResponseFromResult();
        buildRequest(DELETE, BASE_URL + "/" + addUserResponse.getUserUuid());
        makeRequest();
        assertEquals(OK, result.status());
    }

    private AddUserResponse getAddUserResponseFromResult() throws IOException {
        return Json.fromJson(
                new ObjectMapper().readTree(contentAsString(result)),
                AddUserResponse.class
        );
    }

}
