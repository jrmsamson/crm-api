package exceptions;

import com.google.common.base.Joiner;
import play.mvc.Http;

import static util.Constants.IMAGE_CONTENT_TYPE_EXTENSIONS;

public class ImageContentTypeNotSupportedException extends BaseRestException {
    public ImageContentTypeNotSupportedException() {
        super(Http.Status.BAD_REQUEST, "Image Content-Type not supported. Supported: "
                + Joiner.on(", ").join(IMAGE_CONTENT_TYPE_EXTENSIONS.values()));
    }
}
