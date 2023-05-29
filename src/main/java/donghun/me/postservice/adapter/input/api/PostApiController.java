package donghun.me.postservice.adapter.input.api;

import donghun.me.postservice.adapter.input.api.dto.*;
import donghun.me.postservice.application.dto.PostDetailDto;
import donghun.me.postservice.application.dto.PostDto;
import donghun.me.postservice.application.port.input.PostCommandUseCase;
import donghun.me.postservice.application.port.input.PostQueryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostCommandUseCase postCommandUseCase;
    private final PostQueryUseCase postQueryUseCase;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<CreatePost.Response> register(
            @Valid @RequestPart(value = "post") CreatePost.Request request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        Long postId = postCommandUseCase.createPost(request.toCommand(thumbnail));
        return CreatePost.Response.success(postId);
    }

    @DeleteMapping("/{postId}")
    public BaseResponse<DeletePost.Response> delete(@PathVariable Long postId) {
        postCommandUseCase.deletePost(postId);
        return DeletePost.Response.success(postId);
    }

    @PutMapping("/{postId}")
    public BaseResponse<UpdatePost.Response> update(
            @PathVariable Long postId,
            @Valid @RequestPart(value = "post") UpdatePost.Request request,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        postCommandUseCase.updatePost(postId, request.toCommand(thumbnail));
        return UpdatePost.Response.success(postId);
    }

    @GetMapping("/{postId}")
    public BaseResponse<DetailPost.Response> detail(@PathVariable Long postId) {
        PostDetailDto postDetailDto = postQueryUseCase.findById(postId);
        return DetailPost.Response.success(postDetailDto);
    }

    @GetMapping
    public BaseResponse<Page<ListPost.Response>> getPage(Pageable pageable) {
        Page<PostDto> posts = postQueryUseCase.getPage(pageable);
        return ListPost.Response.success(posts);
    }
}
