package functional.controllers;

import enums.Role;
import model.entities.AddUserRequest;
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

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.HttpVerbs.GET;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.route;

public class AuthenticationControllerTest {

    public static final String USERNAME = "jer0Me";
    public static final String PASSWORD = "password";
    private Database database;
    private Application app;

    public AuthenticationControllerTest() {
        app = new GuiceApplicationBuilder().build();
        database = app.injector().instanceOf(Database.class);
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
        setUpFixture();
    }

    private void setUpFixture() {
        AddUserRequest addUserRequest = new AddUserRequest(
                "Jerome", "Samson", Role.USER, USERNAME , PASSWORD
        );

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(addUserRequest))
                .uri("/users");
        route(app, request);
    }

    @Test
    public void shouldLogin() {
        LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(loginRequest))
                .uri("/login");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void shouldLogout() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/logout");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }
}
