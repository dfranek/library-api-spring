package net.dfranek.library.rest.entity;

import net.dfranek.library.rest.dto.FileObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "file")
public class DatabaseFile implements EntityInterface<FileObject> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String path;

    private String mimeType;

    private String fileName;

    private Integer fileSize;

    private String publicPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath;
    }


    @Override
    public FileObject toDto() {
        FileObject file = new FileObject();
        file.setPath(getPublicPath());

        return file;
    }
}
