package exceptions;

import play.mvc.Http;

public class CustomerRequestException extends BaseRestException {
    public CustomerRequestException (String message) {
        super(Http.Status.BAD_REQUEST, message);
    }
}
