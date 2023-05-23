package donghun.me.postservice.adapter.output.persistence.repository;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.common.environment.AbstractDataAccessMysqlTestContainer;
import donghun.me.postservice.fixture.PostEntityFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostQueryRepositoryImplTest extends AbstractDataAccessMysqlTestContainer {

    @Autowired
    private PostQueryRepository postQueryRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;


    @DisplayName("게시글이 존재하면 true를 반환한다.")
    @Test
    void isExistReturnTrue() {
        // given
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();
        postRepository.save(postEntity);

        // when
        boolean result = postQueryRepository.isExist(postEntity.getId());

        // then
        assertTrue(result);
    }

    @DisplayName("게시글이 존재하면 false를 반환한다.")
    @Test
    void isExistReturnFalse() {
        // given

        // when
        boolean result = postQueryRepository.isExist(1L);

        // then
        assertFalse(result);
    }

    @DisplayName("게시글 단건 조회 시 존재하지 않는 경우 null을 반환한다.")
    @Test
    void findByIdReturnNull() {
        // given

        // when
        Optional<PostEntity> result = postQueryRepository.findById(1L);

        // then
        assertTrue(result.isEmpty());
    }

    @DisplayName("게시글 단건 조회 시 연관된 엔티티를 모두 조회한다.")
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
                                        .name("MSA")
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
        Optional<PostEntity> result = postQueryRepository.findById(postEntity.getId());

        // then
        assertTrue(result.isPresent());
        assertThat(result.get()).isNotNull()
                                .extracting("id", "title", "contents", "visible", "thumbnail", "summary")
                                .contains(
                                        postEntity.getId(),
                                        postEntity.getTitle(),
                                        postEntity.getContents(),
                                        postEntity.isVisible(),
                                        postEntity.getThumbnail(),
                                        postEntity.getSummary()
                                );

        assertThat(result.get()
                         .getPostTags())
                .isNotEmpty()
                .hasSize(insertTagEntities.size())
                .extracting("post", "tag")
                .contains(
                        tuple(postEntity, tagEntity1),
                        tuple(postEntity, tagEntity2),
                        tuple(postEntity, tagEntity3)
                );
    }

    @DisplayName("게시글 단건 조회 시 비공개인 경우 null을 반환한다.")
    @Test
    void findByIdVisibleFalseReturnNull() {
        // given
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

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .visible(false)
                                                 .build();

        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);
        postEntity.addTag(tagEntity3);

        postRepository.save(postEntity);

        // when
        Optional<PostEntity> result = postQueryRepository.findById(postEntity.getId());

        // then
        assertTrue(result.isEmpty());
    }

    @DisplayName("게시글 단건 조회 시 id에 해당하는 값이 없는 경우 null을 반환한다.")
    @Test
    void findByIdNotExistReturnNull() {
        // given
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

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .visible(false)
                                                 .build();

        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);
        postEntity.addTag(tagEntity3);

        postRepository.save(postEntity);

        // when
        Optional<PostEntity> result = postQueryRepository.findById(postEntity.getId() + 1L);

        // then
        assertTrue(result.isEmpty());
    }

    @DisplayName("게시글 단건 조회 시 태그가 없는 경우 게시글 도메인을 반환한다.")
    @Test
    void findByIdEmptyTags() {
        // given
        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();

        postRepository.save(postEntity);

        // when
        Optional<PostEntity> result = postQueryRepository.findById(postEntity.getId());

        // then
        assertTrue(result.isPresent());
        assertThat(result.get())
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

        assertThat(result.get()
                         .getPostTags())
                .isNotNull()
                .isEmpty();
    }

    @DisplayName("게시글 목록 조회 시 Pageable을 사용하여 페이징 처리한다.")
    @Test
    void getPage() {
        // given
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
        Page<PostEntity> result = postQueryRepository.getPage(pageable);

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