package unit.services;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import repositories.RepositoryFactory;
import services.UploadService;
import services.impl.UploadServiceImpl;
import util.ConfigPath;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

public class UploadServiceTest {

    private final String TMP_PATH;
    private final String IMAGE_PATH;

    private UploadService uploadService;

    public UploadServiceTest() {
        Application application = new GuiceApplicationBuilder().build();
        RepositoryFactory repositoryFactory = mock(RepositoryFactory.class);
        uploadService = new UploadServiceImpl(repositoryFactory, application);
        TMP_PATH = application.config().getString(ConfigPath.TMP_PATH_CONFIG);
        IMAGE_PATH = application.config().getString(ConfigPath.IMAGES_PATH_CONFIG);
    }

    @Test
    public void shouldMoveImageFromTmpToImageFolder() throws IOException {
        String imageFileName = "myimage.png";
        new File(TMP_PATH + imageFileName).createNewFile();
        uploadService.moveFileToImagesFolder(imageFileName);
        File newFilePath = new File(IMAGE_PATH + imageFileName);
        assertTrue(newFilePath.exists());
        newFilePath.delete();
    }
}
