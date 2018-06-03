package controllers;

import com.google.inject.Inject;
import play.mvc.*;
import services.BaseService;
import services.UserService;
import util.TransactionalAction;

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



}
