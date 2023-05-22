package donghun.me.postservice.adapter.output.persistence.repository;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.domain.model.Tag;

import java.util.Optional;

public interface TagQueryRepository {
    boolean isTagExist(String tag);
    Optional<TagEntity> findByName(String name);
}
