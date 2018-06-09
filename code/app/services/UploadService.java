package services;

import java.io.File;

public interface UploadService extends BaseService {

    File moveImageFromTmpToImagesFolder(String imageFileName);
}
