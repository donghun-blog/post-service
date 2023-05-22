package donghun.me.postservice.adapter.input.api;

import donghun.me.postservice.adapter.input.api.dto.BaseResponse;
import donghun.me.postservice.adapter.input.api.dto.CreatePost;
import donghun.me.postservice.application.port.input.PostCommandUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostCommandUseCase postCommandUseCase;


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<CreatePost.Response> register(
            @Valid @RequestPart(value = "post") CreatePost.Request request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        Long postId = postCommandUseCase.createPost(request.toCommand(thumbnail));
        return CreatePost.Response.success(postId);
    }
}
