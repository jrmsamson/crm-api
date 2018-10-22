package exceptions;

import play.mvc.Http;

import java.io.IOException;

public class ImageUploadException extends BaseRestException {
    public ImageUploadException(Throwable e) {
        super(Http.Status.INTERNAL_SERVER_ERROR, "There was an error trying to process the image", e);
    }
}
