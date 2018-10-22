package exceptions;

import static play.mvc.Http.Status.BAD_REQUEST;

public class CustomerDoesNotExistException extends BaseRestException {

    public CustomerDoesNotExistException() {
        super(BAD_REQUEST, "Customer does not exist");
    }
}
