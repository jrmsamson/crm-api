package functional.controllers;

import controllers.LoginController;
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

public class LoginControllerTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";
    private Database database;
    private Application app;

    public LoginControllerTest() {
        app = new GuiceApplicationBuilder().build();
        database = app.injector().instanceOf(Database.class);
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
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
                .method(POST)
                .bodyJson(Json.toJson(new LoginRequest("admin", "admin")))
                .uri(controllers.routes.LoginController.login().url());
        Http.Session session = route(app, request).session();

        request = new Http.RequestBuilder()
                .method(GET)
                .uri(controllers.routes.LoginController.logout().url())
                .session(session);
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }
}
