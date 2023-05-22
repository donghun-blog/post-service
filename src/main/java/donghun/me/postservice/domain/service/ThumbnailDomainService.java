package donghun.me.postservice.domain.service;

import java.util.UUID;

public class ThumbnailDomainService {
    public static String generate(String path) {
        return String.format("%s.%s", UUID.randomUUID(), getExtension(path));
    }

    public static String getExtension(String path) {
        return path.substring(path.lastIndexOf(".") + 1)
                   .toUpperCase();
    }
}
