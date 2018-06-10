package functional.controllers;

import model.entities.LoginRequest;
import org.junit.After;
import org.junit.Before;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import static play.mvc.Http.HttpVerbs.POST;
import static play.test.Helpers.route;

public class BaseControllerTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "admin";

    private Database database;
    protected Application app;
    protected Http.Session session;
    protected Http.RequestBuilder request;
    protected Result result;

    public BaseControllerTest() {
        app = new GuiceApplicationBuilder().build();
        database = app.injector().instanceOf(Database.class);
    }

    @Before
    public void setUp() {
        Evolutions.applyEvolutions(database);
        doLogin();
    }

    private void doLogin() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.toJson(new LoginRequest(USERNAME, PASSWORD)))
                .uri(controllers.routes.LoginController.login().url());
        session = route(app, request).session();
    }

    @After
    public void tearDown() {
        Evolutions.cleanupEvolutions(database);
        database.shutdown();
    }

    protected Http.RequestBuilder buildRequest(String httpMethod, String uri) {
        request = new Http.RequestBuilder()
                .method(httpMethod)
                .uri(uri)
                .session(session);
        return request;
    }

    protected Result makeRequest() {
        result = route(app, request);
        return result;
    }
}
