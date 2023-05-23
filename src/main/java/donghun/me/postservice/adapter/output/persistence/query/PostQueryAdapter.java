package donghun.me.postservice.adapter.output.persistence.query;

import donghun.me.postservice.adapter.output.persistence.repository.PostQueryRepository;
import donghun.me.postservice.application.port.output.QueryPostPort;
import donghun.me.postservice.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostQueryAdapter implements QueryPostPort {

    private final PostQueryRepository postQueryRepository;

    @Override
    public Post findById(Long postId) {
        return null;
    }

    @Override
    public boolean isPostExist(Long postId) {
        return postQueryRepository.isExist(postId);
    }
}
