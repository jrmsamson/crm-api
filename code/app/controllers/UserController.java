package controllers;

import com.google.inject.Inject;
import model.entities.AddUserRequest;
import play.libs.Json;
import play.mvc.*;
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

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
        init(userService);
    }

    public CompletionStage<Result> getUsersActive() {
        return CompletableFuture
                .supplyAsync(() -> this.userService.getUsersActive())
                .thenApply(users -> ok(Json.toJson(users)));
    }

    public CompletionStage<Result> addUser() {
        AddUserRequest addUserRequest = Json.fromJson(request().body().asJson(), AddUserRequest.class);
        return CompletableFuture
                .runAsync(() ->
                        this.userService.addUser(addUserRequest)
                ).thenApply(aVoid -> ok());
    }

    public CompletionStage<Result> editUser(String uuid) {
        AddUserRequest addUserRequest = Json.fromJson(request().body().asJson(), AddUserRequest.class);
        return CompletableFuture.runAsync(() ->
                this.userService.editUser(UUID.fromString(uuid), addUserRequest)
        ).thenApply(aVoid -> ok());
    }

    public CompletionStage<Result> deleteUser(String uuid) {
        return CompletableFuture.runAsync(() ->
                this.userService.deleteUser(UUID.fromString(uuid))
        ).thenApply(aVoid -> ok());
    }
}
