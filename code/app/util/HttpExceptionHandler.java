package util;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.BaseRestException;
import model.entities.responses.ErrorResponse;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Singleton;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.unauthorized;

@Singleton
public class HttpExceptionHandler {

    public Result handle(Throwable exception) {
        Logger.error("Server error", exception);
        if (exception.getCause() instanceof BaseRestException)
            return handleRestException(
                    (BaseRestException)exception.getCause()
            );

        return internalServerError(getInternalErrorResponseJson());
    }

    private Result handleRestException(BaseRestException exception) {
        if (UNAUTHORIZED == exception.getStatusCode())
            return unauthorized(getErrorResponseJson(exception));

        if (BAD_REQUEST == exception.getStatusCode())
            return badRequest(getErrorResponseJson(exception));

        return internalServerError(getErrorResponseJson(exception));
    }

    private JsonNode getErrorResponseJson(BaseRestException exception) {
        return Json.toJson(new ErrorResponse(exception.getErrorMessage()));
    }

    private JsonNode getInternalErrorResponseJson() {
        return Json.toJson(new ErrorResponse("There was an error trying to execute the action"));
    }

}
