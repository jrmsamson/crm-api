package exceptions;

import static play.mvc.Http.Status.BAD_REQUEST;

public class CustomerWithSameNameAndSurnameAlreadyExistException extends BaseRestException {
    public CustomerWithSameNameAndSurnameAlreadyExistException() {
        super(BAD_REQUEST, "There already exist a customer with the same name and surname");
    }

}
