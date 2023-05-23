package donghun.me.postservice.adapter.output.persistence.repository;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostQueryRepository {
    boolean isExist(Long postId);
    Optional<PostEntity> findById(Long postId);
    Page<PostEntity> getPage(Pageable pageable);
}
