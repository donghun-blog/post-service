package donghun.me.postservice.application.port.input;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadUseCase {
    String upload(MultipartFile image);
}
