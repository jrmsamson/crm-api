package exceptions;

import com.google.common.base.Joiner;
import play.mvc.Http;

import static util.Constants.IMAGE_CONTENT_TYPE_EXTENSIONS;

public class ImageExtensionNotSupportedException extends BaseRestException {

    private static final Integer ERROR_CODE = Http.Status.BAD_REQUEST;
    private static final String MESSAGE = "Image extension not supported. Supported: "
            + Joiner.on(", ").join(IMAGE_CONTENT_TYPE_EXTENSIONS.values());

    public ImageExtensionNotSupportedException() {
        super(ERROR_CODE, MESSAGE);
    }

    public ImageExtensionNotSupportedException(Throwable e) {
        super(ERROR_CODE, MESSAGE, e);
    }
}
