package unit.services;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import repositories.RepositoryFactory;
import services.ImageService;
import services.impl.ImageServiceImpl;
import util.ConfigPath;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

public class ImageServiceTest {

    private final String TMP_PATH;
    private final String IMAGE_PATH;

    private ImageService imageService;

    public ImageServiceTest() {
        Application application = new GuiceApplicationBuilder().build();
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        imageService = new ImageServiceImpl(repositoryFactory, application);
        TMP_PATH = application.config().getString(ConfigPath.TMP_PATH_CONFIG);
        IMAGE_PATH = application.config().getString(ConfigPath.IMAGES_PATH_CONFIG);
    }

    @Test
    public void shouldMoveImageFromTmpToImageFolder() throws IOException {
        String imageFileName = "myimage.png";
        new File(TMP_PATH + imageFileName).createNewFile();
        imageService.moveImageFromTmpToImagesFolder(imageFileName);
        File newFilePath = new File(IMAGE_PATH + imageFileName);
        assertTrue(newFilePath.exists());
        newFilePath.delete();
    }
}
