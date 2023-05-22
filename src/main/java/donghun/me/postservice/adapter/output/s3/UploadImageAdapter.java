package donghun.me.postservice.adapter.output.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import donghun.me.postservice.adapter.output.s3.config.S3Properties;
import donghun.me.postservice.application.port.output.UploadImagePort;
import donghun.me.postservice.domain.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static donghun.me.postservice.domain.exception.PostErrorCode.IMAGE_UPLOAD_FAIL;

@Component
@RequiredArgsConstructor
public class UploadImageAdapter implements UploadImagePort {

    private final AmazonS3Client amazonS3Client;
    private final S3Properties properties;

    @Override
    public void upload(String path, MultipartFile image) {
        try {
            ObjectMetadata metaData = createMetaData(image);
            try (InputStream inputStream = image.getInputStream()) {
                String fullPath = properties.getPrefix() + path;
                amazonS3Client.putObject(
                        new PutObjectRequest(properties.getBucket(), fullPath, inputStream, metaData)
                                .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new FileUploadException();
            }
        } catch (IOException e) {
            throw new PostException(IMAGE_UPLOAD_FAIL);
        }
    }

    private ObjectMetadata createMetaData(MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        objectMetadata.setContentType("image/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        return objectMetadata;
    }
}
