package donghun.me.postservice.adapter.output.persistence.command;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.application.port.output.CommandTagPort;
import donghun.me.postservice.domain.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagCommandAdapter implements CommandTagPort {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public Tag save(String name) {
        TagEntity tagEntity = TagEntity.builder().name(name).build();
        return tagMapper.toDomainModel(tagRepository.save(tagEntity));
    }
}
