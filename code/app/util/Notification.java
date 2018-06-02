package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Notification {

    List<Error> errors;

    public Notification() {
        errors = new ArrayList<>();
    }

    public void addError(String message) {
        errors.add(new Error(message, null));
    }

    public void addError(String message, Exception exception) {
        errors.add(new Error(message, exception));
    }

    public String errorMessage() {
        return errors.stream().map(error ->
                error.message + (error.cause.map(e -> ", " + e.getMessage()).orElse(""))
        ).collect(Collectors.joining("\n"));
    }

    public Boolean hasErrors() {
        return errors.size() > 0;
    }

    private static class Error {
        String message;
        Optional<Exception> cause;

        public Error(String message, Exception cause) {
            this.message = message;
            this.cause = Optional.ofNullable(cause);
        }
    }
}