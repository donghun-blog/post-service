package donghun.me.postservice.adapter.output.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static donghun.me.postservice.adapter.output.persistence.entity.QPostEntity.postEntity;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isExist(Long postId) {
        return queryFactory
                .select(postEntity)
                .from(postEntity)
                .where(postEntity.id.eq(postId))
                .fetchOne() != null;
    }
}
