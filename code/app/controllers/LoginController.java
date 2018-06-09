package controllers;

import model.entities.LoginRequest;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import services.LoginService;
import util.annotation.Secured;
import util.annotation.Transactional;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static util.Constants.ROLE_SESSION_KEY;
import static util.Constants.TOKEN_SESSION_KEY;
import static util.Constants.USER_ID_SESSION_KEY;


@Transactional
public class LoginController extends BaseController {

    private final LoginService loginService;

    private HttpExecutionContext ec;

    @Inject
    public LoginController(LoginService loginService, HttpExecutionContext ec) {
        this.ec = ec;
        this.loginService = loginService;
        init(this.loginService);
    }

    public CompletionStage<Result> login() {
        LoginRequest loginRequest = Json.fromJson(request().body().asJson(), LoginRequest.class);

        return CompletableFuture.supplyAsync(() ->
                loginService.login(loginRequest)
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
        return CompletableFuture.runAsync(loginService::logout).thenApplyAsync(
                aVoid -> {
                    session().clear();
                    return ok();
                },
                ec.current()
        );
    }
}
