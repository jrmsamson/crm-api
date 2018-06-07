package exceptions;

import play.mvc.Http;

public class IncorrectUsernameOrPasswordException extends BaseRestException {

    public IncorrectUsernameOrPasswordException() {
        super(Http.Status.UNAUTHORIZED, "Incorrect username and/or password");
    }
}
