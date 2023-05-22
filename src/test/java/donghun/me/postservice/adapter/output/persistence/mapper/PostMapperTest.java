package donghun.me.postservice.adapter.output.persistence.mapper;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.fixture.PostEntityFixture;
import donghun.me.postservice.fixture.PostFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class PostMapperTest extends AbstractDefaultTest {

    @DisplayName("Post 엔티티에서 Post 도메인으로 매핑 후 값을 확인한다.")
    @Test
    void toDomainModel() {
        // given
        PostMapper postMapper = createPostMapper();
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .build();

        TagEntity tagEntity1 = TagEntity.builder()
                                        .id(1L)
                                        .name("Spring Boot")
                                        .build();
        TagEntity tagEntity2 = TagEntity.builder()
                                        .id(2L)
                                        .name("Spring Security")
                                        .build();
        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        // when
        Post post = postMapper.toDomainModel(postEntity);

        // then
        assertThat(post)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "thumbnail", "summary")
                .contains(
                        postEntity.getId(),
                        postEntity.getTitle(),
                        postEntity.getContents(),
                        postEntity.isVisible(),
                        postEntity.getThumbnail(),
                        postEntity.getSummary()
                );

        assertThat(post.getTags())
                .isNotNull()
                .hasSize(2)
                .extracting("id", "name")
                .contains(
                        tuple(tagEntity1.getId(), tagEntity1.getName()),
                        tuple(tagEntity2.getId(), tagEntity2.getName())
                );

    }

    @DisplayName("Post 엔티티에서 태그가 없는 경우 Post 도메인으로 매핑 후 값을 확인한다.")
    @Test
    void toDomainModelEmptyTags() {
        // given
        PostMapper postMapper = createPostMapper();
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .build();

        // when
        Post post = postMapper.toDomainModel(postEntity);

        // then
        assertThat(post)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "thumbnail", "summary")
                .contains(
                        postEntity.getId(),
                        postEntity.getTitle(),
                        postEntity.getContents(),
                        postEntity.isVisible(),
                        postEntity.getThumbnail(),
                        postEntity.getSummary()
                );

        assertThat(post.getTags())
                .isEmpty();
    }

    @DisplayName("게시글 도메인에서 게시글 엔티티로 변환 시 태그가 없는 경우 값을 확인한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {1L})
    void toEntity(Long id) {
        // given
        PostMapper postMapper = createPostMapper();
        Post post = PostFixture.complete()
                               .id(id)
                               .build();


        // when
        PostEntity entity = postMapper.toEntity(post);

        // then
        assertThat(entity)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "thumbnail", "summary")
                .contains(post.getId(), post.getTitle(), post.getContents(),
                        post.isVisible(), post.getThumbnail(), post.getSummary());

        assertThat(entity.getPostTags())
                .hasSize(post.getTags()
                             .size());
    }

    @DisplayName("게시글 도메인에서 게시글 엔티티로 변환 시 태그가 없는 경우 값을 확인한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {1L})
    void toEntityEmptyTag(Long id) {
        // given
        PostMapper postMapper = createPostMapper();
        Post post = PostFixture.complete()
                               .id(id)
                               .tags(Collections.emptyList())
                               .build();


        // when
        PostEntity entity = postMapper.toEntity(post);

        // then
        assertThat(entity)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "thumbnail", "summary")
                .contains(post.getId(), post.getTitle(), post.getContents(),
                        post.isVisible(), post.getThumbnail(), post.getSummary());

        assertThat(entity.getPostTags())
                .isEmpty();
    }

    private PostMapper createPostMapper() {
        return new PostMapper(new TagMapper());
    }
}