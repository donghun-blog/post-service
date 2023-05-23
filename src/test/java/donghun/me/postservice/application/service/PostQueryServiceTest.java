package donghun.me.postservice.application.service;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.application.dto.PostDetailDto;
import donghun.me.postservice.common.environment.AbstractServiceMysqlTestContainer;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.fixture.PostEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static donghun.me.postservice.domain.exception.PostErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;

class PostQueryServiceTest extends AbstractServiceMysqlTestContainer {

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;


    @DisplayName("게시글 단건 조회 시 게시글이 없는 경우 예외를 반환한다.")
    @Test
    void findByIdNotExistException() {
        // given

        // when & then
        assertThatThrownBy(() -> postQueryService.findById(1L))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_NOT_FOUND);
    }

    @DisplayName("게시글 단건 조회 시 게시글 Dto 반환한다.")
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

        tagRepository.save(tagEntity1);
        tagRepository.save(tagEntity2);
        tagRepository.save(tagEntity3);

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();
        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);
        postEntity.addTag(tagEntity3);

        postRepository.save(postEntity);

        // when
        PostDetailDto result = postQueryService.findById(postEntity.getId());

        // then
        assertThat(result)
                .isNotNull()
                .extracting("id", "title", "contents","visible", "summary")
                .contains(
                        postEntity.getId(),
                        postEntity.getTitle(),
                        postEntity.getContents(),
                        postEntity.isVisible(),
                        postEntity.getSummary()
                );

        assertThat(result.thumbnail())
                .isNotNull();

        assertThat(result.tags())
                .isNotNull()
                .isNotEmpty()
                .extracting("id", "name")
                .contains(
                        tuple(tagEntity1.getId(), tagEntity1.getName()),
                        tuple(tagEntity2.getId(), tagEntity2.getName()),
                        tuple(tagEntity3.getId(), tagEntity3.getName())
                );
    }

    @DisplayName("게시글 단건 조회 시 태그가 존재하지 않는 경우 빈 리스트를 반환한다.")
    @Test
    void findByIdEmptyTags() {
        // given
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();
        postRepository.save(postEntity);

        // when
        PostDetailDto result = postQueryService.findById(postEntity.getId());

        // then
        assertThat(result)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "summary")
                .contains(
                        postEntity.getId(),
                        postEntity.getTitle(),
                        postEntity.getContents(),
                        postEntity.isVisible(),
                        postEntity.getSummary()
                );

        assertThat(result.thumbnail())
                .isNotNull();
        
        assertThat(result.tags())
                .isNotNull()
                .isEmpty();
    }

}