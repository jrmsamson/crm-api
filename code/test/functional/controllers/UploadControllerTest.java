package functional.controllers;

import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import model.entities.FileResponse;
import org.junit.Test;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletionStage;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;
import static util.Constants.IMAGE_FILE_PART_NAME;

public class UploadControllerTest extends BaseControllerTest{

    @Test
    public void shouldUploadAnImage() throws IOException {
        Files.TemporaryFileCreator temporaryFileCreator = app.injector().instanceOf(Files.TemporaryFileCreator.class);
        Materializer materializer = app.injector().instanceOf(Materializer.class);

        Path tempfilePath = createTempFile(null, "tempImage.png");
        write(tempfilePath, "myImage".getBytes("utf-8"));

        Source<ByteString, CompletionStage<IOResult>> source = FileIO.fromPath(tempfilePath);
        Http.MultipartFormData.FilePart<Source<ByteString, ?>> part = new Http.MultipartFormData.FilePart<>(IMAGE_FILE_PART_NAME, "filename", "image/png", source);
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyMultipart(singletonList(part), temporaryFileCreator, materializer)
                .session(session)
                .uri(controllers.routes.UploadController.uploadImage().url());

        Result result = route(app, request);
        FileResponse fileResponse = Json.fromJson(Json.parse(contentAsString(result)), FileResponse.class);

        assertEquals(OK, result.status());
        assertNotNull(fileResponse);
    }

}
