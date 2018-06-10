package functional.controllers;

import enums.Role;
import model.entities.UserRequest;
import model.entities.LoginRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class UserControllerTest {

    private Database database;
    private Application app;
    private Http.Session session;
    private String baseUrl;


    public UserControllerTest() {
        app = new GuiceApplicationBuilder().build();
        database = app.injector().instanceOf(Database.class);
        baseUrl = controllers.routes.UserController.getUsersActive().url();
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
        doLogin();
    }

    private void doLogin() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(new LoginRequest("admin", "admin")))
                .uri(controllers.routes.LoginController.login().url());
        session = route(app, request).session();
    }

    @Test
    public void shouldBeAvailableGetAllUsers() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(baseUrl)
                .session(session);
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void shouldBeAvailableAddANewUser() {
        UserRequest userRequest = new UserRequest(
                "Jerome", "Samson", Role.USER, "jer0Me" , "password"
        );

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(userRequest))
                .uri(baseUrl)
                .session(session);
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void shouldBeAvailableEditUser() {
        UserRequest userRequest = new UserRequest(
                "Jerome", "Samson", Role.USER, "jer0Me" , "password"
        );
        UUID uuid = UUID.randomUUID();
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .bodyJson(Json.toJson(userRequest))
                .uri(baseUrl + "/" + uuid)
                .session(session);
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void shouldBeAvailableDeleteUser() {
        UUID uuid = UUID.randomUUID();
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri(baseUrl + "/" + uuid)
                .session(session);
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        this.database.shutdown();
    }
}
