package services.impl;

import exceptions.ImageDoesNotExistException;
import exceptions.ImageFolderNotCreatedException;
import play.Application;
import repositories.RepositoryFactory;
import services.ImageService;
import util.ConfigPath;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageServiceImpl extends BaseServiceImpl implements ImageService {


    private final String TMP_PATH;
    private final String IMAGES_PATH;

    @Inject
    public ImageServiceImpl(RepositoryFactory repositoryFactory, Application application) {
        super(repositoryFactory);
        TMP_PATH = application.config().getString(ConfigPath.TMP_PATH_CONFIG);
        IMAGES_PATH = application.config().getString(ConfigPath.IMAGES_PATH_CONFIG);
        createImagesPathIfNeeded();
    }

    private void createImagesPathIfNeeded() {
        File file = new File(IMAGES_PATH);
        if (!file.exists()) {
            try {
                Files.createDirectory(file.toPath());
            } catch (IOException e) {
                throw new ImageFolderNotCreatedException();
            }
        }
    }

    public File moveImageFromTmpToImagesFolder(String imageFileName) {
        File tempFile = new File(TMP_PATH + imageFileName);
        File imageFile = new File(IMAGES_PATH + imageFileName);

        try {
            Files.move(tempFile.toPath(), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ImageDoesNotExistException(e);
        }
        return imageFile;
    }
}
