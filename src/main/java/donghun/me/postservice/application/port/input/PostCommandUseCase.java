package donghun.me.postservice.application.port.input;

import donghun.me.postservice.application.dto.CreatePostCommand;

public interface PostCommandUseCase {
    Long createPost(CreatePostCommand command);
}
