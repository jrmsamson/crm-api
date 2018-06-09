package services;

import java.io.File;

public interface ImageService extends BaseService {

    File moveImageFromTmpToImagesFolder(String imageFileName);
}
