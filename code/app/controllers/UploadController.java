package controllers;

import model.entities.FileResponse;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import util.ImageBodyParser;
import util.annotation.Secured;

import java.io.File;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import static util.Constants.IMAGE_FILE_PART_NAME;

@Secured
public class UploadController {

    @BodyParser.Of(ImageBodyParser.class)
    public Result uploadImage() {
        final Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        final Http.MultipartFormData.FilePart<File> filePart = formData.getFile(IMAGE_FILE_PART_NAME);
        return ok(
                Json.toJson(new FileResponse(filePart.getFile().getName()))
        );
    }
}
