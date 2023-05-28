package donghun.me.postservice.adapter.input.api;

import donghun.me.postservice.adapter.input.api.dto.BaseResponse;
import donghun.me.postservice.adapter.input.api.dto.UploadImageResponse;
import donghun.me.postservice.application.port.input.ImageUploadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/images")
public class PostImageUploadApiController {

    private final ImageUploadUseCase imageUploadUseCase;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<UploadImageResponse.Response> upload(
            @RequestPart(value = "image") MultipartFile image) {
        String path = imageUploadUseCase.upload(image);
        return UploadImageResponse.Response.success(path);
    }
}
