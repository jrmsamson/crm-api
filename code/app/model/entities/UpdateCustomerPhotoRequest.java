package model.entities;

import exceptions.ImageExtensionNotSupportedException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UpdateCustomerPhotoRequest {

    private UUID userUuid;
    private File imageFile;
    private String contentType;

    public UpdateCustomerPhotoRequest(UUID userUuid, File imageFile, String contentType) {
        this.userUuid = userUuid;
        this.imageFile = imageFile;
        this.contentType = contentType;
    }

    public void validateImage() {
        try {
            BufferedImage image = ImageIO.read(imageFile);

            if (image == null)
                throw new ImageExtensionNotSupportedException();

        } catch (IOException e) {
            throw new ImageExtensionNotSupportedException(e);
        }
    }

    public UUID getCustomerUuuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
