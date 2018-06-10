package functional.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.routes;
import enums.Role;
import model.entities.AddCustomerResponse;
import model.entities.AddUserResponse;
import model.entities.UserRequest;
import model.entities.LoginRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.Logger;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.util.UUID;

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
        Logger.info(addUserResponse.getUserUuid().toString());
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
