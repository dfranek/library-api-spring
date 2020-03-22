package net.dfranek.library.rest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("file")
public class FileConfig {

    private String avatarStoragePath;
    private String bookStoragePath;

    public String getAvatarStoragePath() {
        return avatarStoragePath;
    }

    public void setAvatarStoragePath(String avatarStoragePath) {
        this.avatarStoragePath = avatarStoragePath;
    }

    public String getBookStoragePath() {
        return bookStoragePath;
    }

    public void setBookStoragePath(String bookStoragePath) {
        this.bookStoragePath = bookStoragePath;
    }
}
