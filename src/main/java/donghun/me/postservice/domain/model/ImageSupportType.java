package donghun.me.postservice.domain.model;

import donghun.me.postservice.domain.service.ImagePathGenerateDomainService;

import java.util.EnumSet;

public enum ImageSupportType {
    JPG,
    JPEG,
    PNG,
    GIF
    ;

    public static boolean isSupport(String thumbnail) {
        String extension = ImagePathGenerateDomainService.getExtension(thumbnail);
        return EnumSet.allOf(ImageSupportType.class)
                      .stream()
                      .anyMatch(e -> e.name().equals(extension.toUpperCase()));
    }

}
