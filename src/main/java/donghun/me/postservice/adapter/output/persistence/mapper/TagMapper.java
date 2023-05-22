package donghun.me.postservice.adapter.output.persistence.mapper;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.domain.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public Tag toDomainModel(TagEntity tagEntity) {
        return Tag.builder()
                  .id(tagEntity.getId())
                  .name(tagEntity.getName())
                  .build();
    }

    public TagEntity toEntity(Tag tag) {
        return TagEntity.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .build();
    }
}
