package model.entities;

public class FileResponse {

    private String fileName;

    public FileResponse() {
    }

    public FileResponse(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
