package donghun.me.postservice.application.port.output;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImagePort {
    void upload(String path, MultipartFile file);
}
