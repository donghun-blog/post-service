package donghun.me.postservice.adapter.output.persistence.query;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.repository.TagQueryRepository;
import donghun.me.postservice.application.port.output.QueryTagPort;
import donghun.me.postservice.domain.exception.PostErrorCode;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagQueryAdapter implements QueryTagPort {
    private final TagQueryRepository tagQueryRepository;
    private final TagMapper tagMapper;

    @Override
    public boolean isTagExist(String tag) {
        return tagQueryRepository.isTagExist(tag);
    }

    @Override
    public Tag findByName(String name) {
        TagEntity tagEntity = tagQueryRepository.findByName(name)
                                                .orElseThrow(() -> new PostException(PostErrorCode.TAG_NOT_FOUND));
        return tagMapper.toDomainModel(tagEntity);
    }
}
