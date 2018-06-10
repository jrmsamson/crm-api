package controllers;

import com.google.inject.Inject;
import model.entities.AddEditLogin;
import model.entities.AddUserResponse;
import model.entities.UserRequest;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import services.LoginService;
import services.UserService;
import util.annotation.Secured;
import util.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Secured
@Transactional
public class UserController extends BaseController {

    private UserService userService;
    private LoginService loginService;
    private HttpExecutionContext ec;

    @Inject
    public UserController(UserService userService, LoginService loginService, HttpExecutionContext ec) {
        this.ec = ec;
        this.userService = userService;
        this.loginService = loginService;
        init(userService);
    }

    public CompletionStage<Result> getUsersActive() {
        return CompletableFuture
                .supplyAsync(() -> this.userService.getUsersActive())
                .thenApply(users -> ok(Json.toJson(users)));
    }

    public CompletionStage<Result> addUser() {
        UserRequest userRequest = Json.fromJson(request().body().asJson(), UserRequest.class);
        return CompletableFuture.supplyAsync(() -> {

                    AddUserResponse addUserResponse = userService.addUser(userRequest);

                    loginService.addLoginForUser(
                            new AddEditLogin(
                                    userRequest.getUsername(),
                                    userRequest.getPassword(),
                                    userService.getUserIdByUuid(addUserResponse.getUserUuid())
                            )
                    );

                    return addUserResponse;

                }).thenApplyAsync(addUserResponse ->
                        created(Json.toJson(addUserResponse)),
                        ec.current()
                );
    }

    public CompletionStage<Result> editUser(String uuid) {
        UserRequest userRequest = Json.fromJson(request().body().asJson(), UserRequest.class);
        return CompletableFuture.runAsync(() -> {
                loginService.editLogin(
                        new AddEditLogin(
                                userRequest.getUsername(),
                                userRequest.getPassword(),
                                userService.getUserIdByUuid(UUID.fromString(uuid))
                        )
                );
                userService.editUser(UUID.fromString(uuid), userRequest);

        }).thenApplyAsync(aVoid -> ok(), ec.current());
    }

    public CompletionStage<Result> deleteUser(String uuid) {
        return CompletableFuture.runAsync(() -> {
                    loginService.deleteLoginByUserId(
                            userService.getUserIdByUuid(UUID.fromString(uuid))
                    );
                    userService.deleteUser(UUID.fromString(uuid));
                }

        ).thenApplyAsync(aVoid -> ok(), ec.current());
    }
}
