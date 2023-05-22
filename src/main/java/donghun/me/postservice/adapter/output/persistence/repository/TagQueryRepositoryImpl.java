package donghun.me.postservice.adapter.output.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static donghun.me.postservice.adapter.output.persistence.entity.QTagEntity.tagEntity;

@Repository
@RequiredArgsConstructor
public class TagQueryRepositoryImpl implements TagQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isTagExist(String name) {
        return queryFactory
                .select(tagEntity)
                .from(tagEntity)
                .where(tagEntity.name.eq(name))
                .fetchOne() != null;
    }

    @Override
    public Optional<TagEntity> findByName(String name) {
        return Optional.ofNullable(
                queryFactory
                        .select(tagEntity)
                        .from(tagEntity)
                        .where(tagEntity.name.eq(name))
                        .fetchOne()
        );
    }
}
