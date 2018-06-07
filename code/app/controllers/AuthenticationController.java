package controllers;

import model.entities.LoginRequest;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import services.AuthenticationService;
import util.annotation.Secured;
import util.annotation.Transactional;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static util.Constants.ROLE_SESSION_KEY;
import static util.Constants.TOKEN_SESSION_KEY;
import static util.Constants.USER_ID_SESSION_KEY;


@Transactional
public class AuthenticationController extends BaseController {

    private final AuthenticationService authenticationService;

    private HttpExecutionContext ec;

    @Inject
    public AuthenticationController(AuthenticationService authenticationService, HttpExecutionContext ec) {
        this.ec = ec;
        this.authenticationService = authenticationService;
        init(authenticationService);
    }

    public CompletionStage<Result> login() {
        LoginRequest loginRequest = Json.fromJson(request().body().asJson(), LoginRequest.class);

        return CompletableFuture.supplyAsync(() ->
                authenticationService.login(loginRequest)
        ).thenApplyAsync(userSession -> {
            session(USER_ID_SESSION_KEY, userSession.getUserId().toString());
            session(ROLE_SESSION_KEY, userSession.getUserId().toString());
            session(TOKEN_SESSION_KEY, userSession.getToken());
            return ok();
            }, ec.current()
        );
    }

    @Secured
    public CompletionStage<Result> logout() {
        return CompletableFuture.runAsync(authenticationService::logout).thenApplyAsync(
                aVoid -> {
                    session().clear();
                    return ok();
                },
                ec.current()
        );
    }
}
