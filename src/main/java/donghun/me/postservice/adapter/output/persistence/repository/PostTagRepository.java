package donghun.me.postservice.adapter.output.persistence.repository;

import donghun.me.postservice.adapter.output.persistence.entity.PostTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {
}
