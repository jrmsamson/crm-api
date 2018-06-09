package exceptions;

import play.mvc.Http;

public class ImageUploadException extends BaseRestException {
    public ImageUploadException() {
        super(Http.Status.INTERNAL_SERVER_ERROR, "There was an error trying to process the image");
    }
}
