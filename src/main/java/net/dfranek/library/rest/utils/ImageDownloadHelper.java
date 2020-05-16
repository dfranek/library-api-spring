package net.dfranek.library.rest.utils;

import net.dfranek.library.rest.dto.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

public final class ImageDownloadHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ImageDownloadHelper.class);

    public static FileObject downloadImage(final String url) {
        try {
            URL imageUrlObj = new URL(url);
            final URLConnection urlConnection = imageUrlObj.openConnection();
            final InputStream inputStream = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            FileObject file = new FileObject();
            file.setContents(Base64.getEncoder().encodeToString(buffer.toByteArray()));
            file.setFileName("image.jpg");
            file.setMimeType(urlConnection.getContentType());
            file.setFileSize(urlConnection.getContentLength());

            return file;
        } catch (IOException e) {
            LOG.warn("error fetching image", e);
        }
        return null;
    }

}
