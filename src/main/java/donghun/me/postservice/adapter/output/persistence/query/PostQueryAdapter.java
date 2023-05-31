package donghun.me.postservice.adapter.output.persistence.query;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.PostMapper;
import donghun.me.postservice.adapter.output.persistence.repository.PostQueryRepository;
import donghun.me.postservice.application.dto.SearchCondition;
import donghun.me.postservice.application.port.output.QueryPostPort;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static donghun.me.postservice.domain.exception.PostErrorCode.POST_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PostQueryAdapter implements QueryPostPort {

    private final PostQueryRepository postQueryRepository;
    private final PostMapper postMapper;

    @Override
    public Post findById(Long postId) {
        PostEntity postEntity = postQueryRepository.findById(postId)
                                                   .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        return postMapper.toDomainModel(postEntity);
    }

    @Override
    public boolean isPostExist(Long postId) {
        return postQueryRepository.isExist(postId);
    }

    @Override
    public Page<Post> getPage(Pageable pageable, SearchCondition condition) {
        Page<PostEntity> posts = postQueryRepository.getPage(pageable, condition);
        return posts.map(postMapper::toDomainModel);
    }
}
