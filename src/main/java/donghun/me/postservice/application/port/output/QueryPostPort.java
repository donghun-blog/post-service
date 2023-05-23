package donghun.me.postservice.application.port.output;

import donghun.me.postservice.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryPostPort {
    Post findById(Long postId);
    boolean isPostExist(Long postId);
    Page<Post> getPage(Pageable pageable);
}
