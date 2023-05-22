package donghun.me.postservice.adapter.output.persistence.repository;

import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
