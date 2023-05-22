package donghun.me.postservice.domain.model;

import donghun.me.postservice.domain.service.ThumbnailDomainService;

import java.util.EnumSet;

public enum ImageSupportType {
    JPG,
    JPEG,
    PNG,
    GIF
    ;

    public static boolean isSupport(String thumbnail) {
        String extension = ThumbnailDomainService.getExtension(thumbnail);
        return EnumSet.allOf(ImageSupportType.class)
                      .stream()
                      .anyMatch(e -> e.name().equals(extension.toUpperCase()));
    }

}
