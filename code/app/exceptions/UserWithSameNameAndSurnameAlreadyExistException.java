package exceptions;

import play.mvc.Http;

public class UserWithSameNameAndSurnameAlreadyExistException extends BaseRestException {

    public UserWithSameNameAndSurnameAlreadyExistException() {
        super(Http.Status.BAD_REQUEST, "There already exist an user with the same name and surname");
    }
}
