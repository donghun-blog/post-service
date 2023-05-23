package donghun.me.postservice.adapter.output.persistence.query;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.mapper.PostMapper;
import donghun.me.postservice.adapter.output.persistence.mapper.TagMapper;
import donghun.me.postservice.adapter.output.persistence.repository.PostQueryRepository;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.common.environment.AbstractDataAccessMysqlTestContainer;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.domain.model.Post;
import donghun.me.postservice.fixture.PostEntityFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static donghun.me.postservice.domain.exception.PostErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostQueryAdapterTest extends AbstractDataAccessMysqlTestContainer {

    @TestConfiguration
    static class PostQueryAdapterTestConfiguration {
        @Autowired
        private PostQueryRepository postQueryRepository;

        @Bean
        TagMapper tagMapper() {
            return new TagMapper();
        }

        @Bean
        PostMapper postMapper() {
            return new PostMapper(tagMapper());
        }

        @Bean
        PostQueryAdapter postQueryAdapter() {
            return new PostQueryAdapter(postQueryRepository, postMapper());
        }
    }

    @Autowired
    private PostQueryAdapter postQueryAdapter;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;

    @DisplayName("게시글이 존재하는 경우 true를 반환한다.")
    @Test
    void isPostExistReturnTrue() {
        // given
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();
        postRepository.save(postEntity);

        // when
        boolean result = postQueryAdapter.isPostExist(postEntity.getId());

        // then
        assertTrue(result);
    }

    @DisplayName("게시글이 존재하지 않는 경우 false를 반환한다.")
    @Test
    void isPostNotExistReturnFalse() {
        // given

        // when
        boolean result = postQueryAdapter.isPostExist(1L);

        // then
        assertFalse(result);
    }

    @DisplayName("게시글 조회 시 게시글이 존재하지 않는 경우 예외를 반환한다.")
    @Test
    void findByIdNotFoundException() {
        // given

        // when & then
        assertThatThrownBy(() -> postQueryAdapter.findById(1L))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_NOT_FOUND)
        ;
    }

    @DisplayName("게시글 조회 시 게시글이 존재하는 경우 게시글 도메인을 반환한다.")
    @Test
    void findById() {
        // given
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();

        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();

        TagEntity tagEntity3 = TagEntity.builder()
                                        .name("Spring Cloud")
                                        .build();

        List<TagEntity> insertTagEntities = List.of(
                tagEntity1,
                tagEntity2,
                tagEntity3
        );
        tagRepository.saveAll(insertTagEntities);

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();
        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);
        postEntity.addTag(tagEntity3);

        postRepository.save(postEntity);

        // when
        Post post = postQueryAdapter.findById(postEntity.getId());

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
                .isNotEmpty()
                .hasSize(insertTagEntities.size())
                .extracting("id", "name")
                .contains(
                        tuple(tagEntity1.getId(), tagEntity1.getName()),
                        tuple(tagEntity2.getId(), tagEntity2.getName()),
                        tuple(tagEntity3.getId(), tagEntity3.getName())
                );

    }

    @DisplayName("게시글 조회 시 태그가 존재하는 경우 게시글 도메인을 반환한다.")
    @Test
    void findByIdNotExistTags() {
        // given

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();
        postRepository.save(postEntity);

        // when
        Post post = postQueryAdapter.findById(postEntity.getId());

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

    @DisplayName("게시글 목록 조회 시 Pageable을 사용하여 페이징 처리 후 Post 도메인으로 변환한다.")
    @Test
    void getPage() {
        final int size = 20;
        final int count = 100;
        List<TagEntity> tagEntities = saveTag();

        List<PostEntity> postEntities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PostEntity postEntity = PostEntityFixture.complete()
                                                     .id(null)
                                                     .build();
            postEntity.addTag(tagEntities.get(0));
            postEntity.addTag(tagEntities.get(1));
            postEntity.addTag(tagEntities.get(2));
            postEntities.add(postEntity);
        }

        postRepository.saveAll(postEntities);
        Pageable pageable = PageRequest.of(0, size);

        // when
        Page<Post> result = postQueryAdapter.getPage(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(count);
        assertThat(result.getSize()).isEqualTo(size);
    }

    private List<TagEntity> saveTag() {
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();

        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();

        TagEntity tagEntity3 = TagEntity.builder()
                                        .name("MSA")
                                        .build();

        List<TagEntity> insertTagEntities = List.of(
                tagEntity1,
                tagEntity2,
                tagEntity3
        );

        tagRepository.saveAll(insertTagEntities);
        return insertTagEntities;
    }
}