package exceptions;

import play.mvc.Http;

public class ImageDoesNotExistException extends BaseRestException{

    public ImageDoesNotExistException(Throwable e) {
        super(Http.Status.BAD_REQUEST, "Image does not exist", e);
    }
}
