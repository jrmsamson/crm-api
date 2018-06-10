package util;

import model.entities.responses.ErrorResponse;
import play.Logger;
import play.http.HttpErrorHandler;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ErrorHandler implements HttpErrorHandler {

    private final HttpExceptionHandler httpExceptionHandler;

    @Inject
    public ErrorHandler(HttpExceptionHandler httpExceptionHandler) {
        this.httpExceptionHandler = httpExceptionHandler;
    }

    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        Logger.error("Client error " + statusCode + ": " + message);
        return CompletableFuture.completedFuture(
                Results.status(statusCode, Json.toJson(new ErrorResponse("Bad request")))
        );
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(
                this.httpExceptionHandler.handle(exception)
        );
    }
}
