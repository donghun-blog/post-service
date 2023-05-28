package donghun.me.postservice.application.service;

import donghun.me.postservice.adapter.output.s3.config.S3Properties;
import donghun.me.postservice.application.port.input.ImageUploadUseCase;
import donghun.me.postservice.application.port.output.UploadImagePort;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.service.ImagePathGenerateDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static donghun.me.postservice.domain.exception.PostErrorCode.IMAGE_EMPTY;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ImageUploadService implements ImageUploadUseCase {

    private final UploadImagePort uploadImagePort;
    private final S3Properties s3Properties;

    @Override
    public String upload(MultipartFile image) {
        if (isNull(image) || image.isEmpty()) {
            throw new PostException(IMAGE_EMPTY);
        }
        String path = ImagePathGenerateDomainService.generate(image.getOriginalFilename());
        uploadImagePort.upload(path, image);
        return getAbsolutePath(path);
    }

    private String getAbsolutePath(String path) {
        return String.format("%s%s", s3Properties.getAbsolutePath(), path);
    }
}
