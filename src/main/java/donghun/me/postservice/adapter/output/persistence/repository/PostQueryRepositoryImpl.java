package donghun.me.postservice.adapter.output.persistence.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.application.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Wildcard.count;
import static donghun.me.postservice.adapter.output.persistence.entity.QPostEntity.postEntity;
import static donghun.me.postservice.adapter.output.persistence.entity.QPostTagEntity.postTagEntity;
import static donghun.me.postservice.adapter.output.persistence.entity.QTagEntity.tagEntity;
import static org.springframework.util.StringUtils.hasText;

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

    @Override
    public Optional<PostEntity> findById(Long postId) {
        return Optional.ofNullable(queryFactory
                .select(postEntity)
                .from(postEntity)
                .leftJoin(postEntity.postTags, postTagEntity)
                .fetchJoin()
                .leftJoin(postTagEntity.tag, tagEntity)
                .fetchJoin()
                .where(
                        postEntity.id.eq(postId),
                        isVisible()
                )
                .fetchOne());
    }

    @Override
    public Page<PostEntity> getPage(Pageable pageable, SearchCondition condition) {
        List<PostEntity> content = queryFactory
                .select(postEntity)
                .from(postEntity)
                .leftJoin(postEntity.postTags, postTagEntity)
                .fetchJoin()
                .leftJoin(postTagEntity.tag, tagEntity)
                .fetchJoin()
                .where(
                        isVisible(),
                        titleEq(condition.keyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(postEntity.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(count)
                .from(postEntity)
                .where(
                        isVisible(),
                        titleEq(condition.keyword())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleEq(String word) {
        return hasText(word) ? postEntity.title.contains(word) : null;
    }

    private BooleanExpression isVisible() {
        return postEntity.visible.eq(true);
    }
}
