package controllers;

import model.entities.LoginRequest;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import play.mvc.With;
import services.AuthenticationService;
import util.TransactionalAction;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class AuthenticationController extends BaseController {

    private static final String TOKEN_KEY = "token";

    private final AuthenticationService authenticationService;

    private HttpExecutionContext ec;

    @Inject
    public AuthenticationController(AuthenticationService authenticationService, HttpExecutionContext ec) {
        this.ec = ec;
        this.authenticationService = authenticationService;
        init(authenticationService);
    }

    @With(TransactionalAction.class)
    public CompletionStage<Result> login() {
        LoginRequest loginRequest = Json.fromJson(request().body().asJson(), LoginRequest.class);

        return CompletableFuture.supplyAsync(() ->
                authenticationService.login(loginRequest)
        ).thenApplyAsync(token -> {
                    session(TOKEN_KEY, token);
                    return ok();
                },ec.current()
        );
    }

    @With(TransactionalAction.class)
    public Result logout() {
        // TODO remove token from database;
        session().clear();
        return ok();
    }
}
