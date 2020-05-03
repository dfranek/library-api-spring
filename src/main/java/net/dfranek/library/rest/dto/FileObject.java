package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.dfranek.library.rest.entity.DatabaseFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileObject implements DtoInterface<DatabaseFile> {

    private static final Map<String, String> endings = new HashMap<>();
    static {
        endings.put("image/jpeg", ".jpg");
        endings.put("image/gif", ".gif");
        endings.put("image/png", ".png");
    }

    private String contents;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_size")
    private Integer fileSize;

    private String path;

    private String storagePath;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void save(String directory) throws IOException {
        byte[] contentAsBytes = Base64.getDecoder().decode(contents);
        if(endings.containsKey(mimeType)) {
            Path filePath = Files.createTempFile(Paths.get(directory), "avatar_", endings.get(mimeType));
            Files.write(filePath, contentAsBytes);

            storagePath = filePath.toAbsolutePath().toString();
            path = "/" + filePath.subpath(filePath.getNameCount() - 3, filePath.getNameCount()).toString();
        }
    }

    public DatabaseFile toEntity() {
        DatabaseFile file = new DatabaseFile();
        file.setFileName(fileName);
        file.setFileSize(fileSize);
        file.setMimeType(mimeType);
        file.setPath(storagePath);
        file.setPublicPath(path);

        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileObject that = (FileObject) o;
        return Objects.equals(contents, that.contents) &&
                Objects.equals(mimeType, that.mimeType) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(fileSize, that.fileSize) &&
                Objects.equals(path, that.path) &&
                Objects.equals(storagePath, that.storagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, mimeType, fileName, fileSize, path, storagePath);
    }
}
