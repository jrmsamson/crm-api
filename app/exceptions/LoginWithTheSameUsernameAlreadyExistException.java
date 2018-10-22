package exceptions;

import play.mvc.Http;

public class LoginWithTheSameUsernameAlreadyExistException extends BaseRestException {

    public LoginWithTheSameUsernameAlreadyExistException() {
        super(Http.Status.BAD_REQUEST, "There already exist an user with the same login username");
    }
}
