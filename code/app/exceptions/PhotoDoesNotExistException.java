package exceptions;

import play.mvc.Http;

public class PhotoDoesNotExistException extends BaseRestException{

    public PhotoDoesNotExistException() {
        super(Http.Status.BAD_REQUEST, "Photo does not exist");
    }
}
