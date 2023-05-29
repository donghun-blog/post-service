package donghun.me.postservice.application.port.input;

import donghun.me.postservice.application.dto.CreatePostCommand;
import donghun.me.postservice.application.dto.UpdatePostCommand;

public interface PostCommandUseCase {
    Long createPost(CreatePostCommand command);
    void deletePost(Long postId);
    void updatePost(Long postId, UpdatePostCommand command);
}
