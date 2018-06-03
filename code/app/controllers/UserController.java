package controllers;

import com.google.inject.Inject;
import model.entities.UserRequest;
import play.libs.Json;
import play.mvc.*;
import services.UserService;
import util.TransactionalAction;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserController extends BaseController {

    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
        init(userService);
    }

    @With(TransactionalAction.class)
    public CompletionStage<Result> getUsersActive() {
        return CompletableFuture
                .supplyAsync(() -> this.userService.getUsersActive())
                .thenApply(users -> ok());
    }

    @With(TransactionalAction.class)
    public CompletionStage<Result> addUser() {
        UserRequest userRequest = Json.fromJson(request().body().asJson(), UserRequest.class);
        return CompletableFuture
                .supplyAsync(() ->
                        this.userService.addUser(userRequest)
                ).thenApply(userUuid ->
                        ok(Json.toJson(userUuid))
                );
    }

    @With(TransactionalAction.class)
    public CompletionStage<Result> editUser(String uuid) {
        UserRequest userRequest = Json.fromJson(request().body().asJson(), UserRequest.class);
        return CompletableFuture.runAsync(() ->
                this.userService.editUser(UUID.fromString(uuid), userRequest)
        ).thenApply(userUuid -> ok());
    }

    @With(TransactionalAction.class)
    public CompletionStage<Result> deleteUser(String uuid) {
        return CompletableFuture.runAsync(() ->
                this.userService.deleteUser(UUID.fromString(uuid))
        ).thenApply(userUuid -> ok());
    }
}
