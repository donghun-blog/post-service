package donghun.me.postservice.application.port.output;

import donghun.me.postservice.domain.model.Post;

public interface QueryPostPort {
    Post findById(Long postId);
    boolean isPostExist(Long postId);
}
