package util;


import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;
import com.google.inject.Inject;
import exceptions.ImageContentTypeNotSupportedException;
import exceptions.ImageUploadException;
import play.api.http.HttpErrorHandler;
import play.core.parsers.Multipart;
import play.libs.streams.Accumulator;
import play.mvc.BodyParser;
import play.mvc.Http;
import scala.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static util.Constants.IMAGE_CONTENT_TYPE_EXTENSIONS;

public class ImageBodyParser extends BodyParser.DelegatingMultipartFormDataBodyParser<File> {

    @Inject
    public ImageBodyParser(Materializer materializer, play.api.http.HttpConfiguration config, HttpErrorHandler errorHandler) {
        super(materializer, config.parser().maxDiskBuffer(), errorHandler);
    }

    @Override
    public Function<Multipart.FileInfo, Accumulator<ByteString, Http.MultipartFormData.FilePart<File>>> createFilePartHandler() {
        return this::apply;
    }

    private Accumulator<ByteString, Http.MultipartFormData.FilePart<File>> apply(Multipart.FileInfo fileInfo) {
        File tempImageFile = generateTempImageFile(fileInfo.contentType());
        Sink<ByteString, CompletionStage<IOResult>> sink = FileIO.toFile(tempImageFile);
        return Accumulator.fromSink(
                sink.mapMaterializedValue(completionStage ->
                        completionStage.thenApplyAsync(
                                results ->
                                        new Http.MultipartFormData.FilePart<>(
                                                fileInfo.partName(),
                                                fileInfo.fileName(),
                                                fileInfo.contentType().getOrElse(null),
                                                tempImageFile
                                        )
                        )
                ));
    }

    private File generateTempImageFile(Option<String> contentTypeOption) {

        if (!contentTypeOption.isDefined()
                || !IMAGE_CONTENT_TYPE_EXTENSIONS.containsKey(contentTypeOption.get()))
            throw new ImageContentTypeNotSupportedException();
        try {
            String extensionFile = IMAGE_CONTENT_TYPE_EXTENSIONS.get(contentTypeOption.get());
            Path path = Files.createTempFile("", extensionFile);
            return path.toFile();
        } catch (IOException e) {
            throw new ImageUploadException();
        }
    }
}


