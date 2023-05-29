package donghun.me.postservice.application.service;

import donghun.me.postservice.adapter.output.persistence.entity.PostEntity;
import donghun.me.postservice.adapter.output.persistence.entity.TagEntity;
import donghun.me.postservice.adapter.output.persistence.repository.PostRepository;
import donghun.me.postservice.adapter.output.persistence.repository.PostTagRepository;
import donghun.me.postservice.adapter.output.persistence.repository.TagRepository;
import donghun.me.postservice.application.dto.CreatePostCommand;
import donghun.me.postservice.application.dto.UpdatePostCommand;
import donghun.me.postservice.application.port.output.UploadImagePort;
import donghun.me.postservice.common.EmptyParameters;
import donghun.me.postservice.common.environment.AbstractServiceMysqlTestContainer;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.fixture.CreatePostCommandFixture;
import donghun.me.postservice.fixture.PostEntityFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static donghun.me.postservice.domain.exception.PostErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

class PostCommandServiceTestService extends AbstractServiceMysqlTestContainer {

    @MockBean
    private UploadImagePort uploadImagePort;

    @Autowired
    private PostCommandService postCommandService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @DisplayName("데이터베이스에 태그가 없는 경우 신규 태그 등록 후 게시글 도메인 등록한 다음 값을 확인한다.")
    @Test
    void createPostNewTag() {
        // given
        CreatePostCommand command = CreatePostCommandFixture.complete()
                                                            .build();
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());

        // when
        Long postId = postCommandService.createPost(command);

        // then
        Optional<PostEntity> findPostEntity = postRepository.findById(postId);
        assertTrue(findPostEntity.isPresent());
        PostEntity postEntity = findPostEntity.get();
        assertThat(postEntity)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "summary")
                .contains(postId,
                        command.title(),
                        command.contents(),
                        command.visible(),
                        command.summary()
                );
        assertThat(postEntity.getThumbnail())
                .isNotBlank();

        assertThat(postEntity.getPostTags()).hasSize(5);
        assertThat(postEntity.getPostTags()
                             .get(0)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(0));
        assertThat(postEntity.getPostTags()
                             .get(1)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(1));
        assertThat(postEntity.getPostTags()
                             .get(2)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(2));
        assertThat(postEntity.getPostTags()
                             .get(3)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(3));
        assertThat(postEntity.getPostTags()
                             .get(4)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(4));
    }

    @DisplayName("데이터베이스에 태그가 몇개만 있고 신규 태그 등록 후 게시글 도메인 등록한 다음 값을 확인한다.")
    @Test
    void createPostLittleTag() {
        // given
        CreatePostCommand command = CreatePostCommandFixture.complete()
                                                            .build();
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());

        tagRepository.save(TagEntity.builder()
                                    .name("ELK")
                                    .build());
        tagRepository.save(TagEntity.builder()
                                    .name("JPA")
                                    .build());
        tagRepository.save(TagEntity.builder()
                                    .name("AWS")
                                    .build());

        // when
        Long postId = postCommandService.createPost(command);

        // then
        Optional<PostEntity> findPostEntity = postRepository.findById(postId);
        assertTrue(findPostEntity.isPresent());
        PostEntity postEntity = findPostEntity.get();
        assertThat(postEntity)
                .isNotNull()
                .extracting("id", "title", "contents", "visible", "summary")
                .contains(postId,
                        command.title(),
                        command.contents(),
                        command.visible(),
                        command.summary()
                );
        assertThat(postEntity.getThumbnail())
                .isNotBlank();

        assertThat(postEntity.getPostTags()).hasSize(5);
        assertThat(postEntity.getPostTags()
                             .get(0)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(0));
        assertThat(postEntity.getPostTags()
                             .get(1)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(1));
        assertThat(postEntity.getPostTags()
                             .get(2)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(2));
        assertThat(postEntity.getPostTags()
                             .get(3)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(3));
        assertThat(postEntity.getPostTags()
                             .get(4)
                             .getTag())
                .extracting("name")
                .isEqualTo(command.tags()
                                  .get(4));

        List<TagEntity> tagEntities = tagRepository.findAll();
        assertThat(tagEntities)
                .hasSize(7);
    }

    @DisplayName("게시글 생성 시 썸네일이 없는 경우 본문에서 첫번째 이미지가 썸네일 이미지가 된다.")
    @Test
    void createPostEmptyThumbnailAndContentsFirstImage() {
        // given
        CreatePostCommand command = CreatePostCommandFixture.complete()
                                                            .thumbnail(null)
                                                            .build();
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());

        // when
        Long postId = postCommandService.createPost(command);

        // then
        Optional<PostEntity> findPostEntity = postRepository.findById(postId);
        assertTrue(findPostEntity.isPresent());
        PostEntity postEntity = findPostEntity.get();
        assertThat(postEntity.getThumbnail()).isEqualTo(CreatePostCommandFixture.getFirstImage());
    }

    @DisplayName("게시글 생성 시 썸네일이 있는 경우 랜덤ID값이 할당된다.")
    @Test
    void createPostExistThumbnailAndRandomId() {
        // given
        CreatePostCommand command = CreatePostCommandFixture.complete()
                                                            .build();
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());

        // when
        Long postId = postCommandService.createPost(command);

        // then
        Optional<PostEntity> findPostEntity = postRepository.findById(postId);
        assertTrue(findPostEntity.isPresent());
        PostEntity postEntity = findPostEntity.get();
        assertThat(postEntity.getThumbnail()).isNotBlank();
    }

    @DisplayName("게시글 생성 시 제목이 없는 경우 예외를 반환한다.")
    @EmptyParameters
    void createPostEmptyTitleException(String emptyTitle) {
        // given
        CreatePostCommand command = CreatePostCommandFixture.complete()
                                                            .title(emptyTitle)
                                                            .build();
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());

        // when & then
        assertThatThrownBy(() -> postCommandService.createPost(command))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_TITLE_EMPTY);
    }

    @DisplayName("게시글 생성 시 본문이 없는 경우 예외를 반환한다.")
    @EmptyParameters
    void createPostEmptyContentsException(String emptyContents) {
        // given
        CreatePostCommand command = CreatePostCommandFixture.complete()
                                                            .contents(emptyContents)
                                                            .build();
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());

        // when & then
        assertThatThrownBy(() -> postCommandService.createPost(command))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_CONTENTS_EMPTY);
    }

    @DisplayName("게시글 생성 시 썸네일이 지원하지 않는 확장자인 경우 예외를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "test.svg",
            "test.bmp",
            "test.tiff"
    })
    void createPostNotSupportFormatThumbnailException(String thumbnail) {
        // given
        CreatePostCommand command = CreatePostCommandFixture.complete()
                                                            .thumbnail(new MockMultipartFile(
                                                                    "thumbnail",
                                                                    thumbnail,
                                                                    MULTIPART_FORM_DATA_VALUE,
                                                                    "test image".getBytes()
                                                            ))
                                                            .build();
        willDoNothing().given(uploadImagePort)
                       .upload(anyString(), any());

        // when & then
        assertThatThrownBy(() -> postCommandService.createPost(command))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", IMAGE_EXTENSION_NOT_SUPPORT);
    }

    @DisplayName("게시글 삭제 시 게시글이 존재하지 않는 경우 예외를 반환한다.")
    @Test
    void deletePostPostExistException() {
        // given

        // when & then
        Assertions.assertThatThrownBy(() -> postCommandService.deletePost(1L))
                  .isInstanceOf(PostException.class)
                  .hasFieldOrPropertyWithValue("errorCode", POST_NOT_FOUND);
    }

    @DisplayName("게시글 존재하는 경우 삭제 후 삭제되었는지 확인한다.")
    @Test
    void deletePost() {
        // given
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();
        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();

        tagRepository.save(tagEntity1);
        tagRepository.save(tagEntity2);

        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();
        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        postRepository.save(postEntity);

        // when
        postCommandService.deletePost(postEntity.getId());

        // then
        assertThat(tagRepository.findAll())
                .hasSize(2);
        assertThat(postTagRepository.findAll())
                .hasSize(0);
        assertThat(postRepository.findAll())
                .hasSize(0);
    }

    @DisplayName("게시글 엔티티를 업데이트 후 값을 확인한다.")
    @Test
    void update() {
        // given
        TagEntity tagEntity1 = TagEntity.builder()
                                        .name("Spring Boot")
                                        .build();

        TagEntity tagEntity2 = TagEntity.builder()
                                        .name("Spring Security")
                                        .build();

        tagRepository.save(tagEntity1);
        tagRepository.save(tagEntity2);


        PostEntity postEntity = PostEntityFixture.complete()
                                                 .id(null)
                                                 .build();

        postEntity.addTag(tagEntity1);
        postEntity.addTag(tagEntity2);

        postRepository.save(postEntity);

        final String updateTitle = "update Title";
        final String updateContents = "update Contents";
        final boolean updateVisible = false;
        final String updateSummary = "updateSummary";
        final List<String> updateTags = List.of(
                "update Tag1",
                "update Tag2",
                "update Tag3"
        );

        final String updateThumbnailName = "update_Tesg.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "thumbnail",
                updateThumbnailName,
                MULTIPART_FORM_DATA_VALUE,
                updateThumbnailName.getBytes());


        UpdatePostCommand command = UpdatePostCommand.builder()
                                                     .title(updateTitle)
                                                     .contents(updateContents)
                                                     .visible(updateVisible)
                                                     .summary(updateSummary)
                                                     .tags(updateTags)
                                                     .thumbnail(mockMultipartFile)
                                                     .build();

        // when
        postCommandService.updatePost(postEntity.getId(), command);

        // then
        PostEntity findPostEntity = postRepository.findById(postEntity.getId())
                                                  .orElseThrow();

        assertThat(findPostEntity)
                .isNotNull()
                .extracting("title", "contents", "visible", "summary")
                .contains(
                        updateTitle,
                        updateContents,
                        updateVisible,
                        updateSummary
                );

        assertThat(findPostEntity.getThumbnail())
                .isNotBlank();

        assertThat(findPostEntity.getPostTags())
                .isNotNull()
                .hasSize(updateTags.size());

        assertThat(findPostEntity.getPostTags()
                                 .get(0)
                                 .getTag())
                .isNotNull()
                .extracting("name")
                        .isEqualTo(updateTags.get(0));

        assertThat(findPostEntity.getPostTags()
                                 .get(1)
                                 .getTag())
                .isNotNull()
                .extracting("name")
                .isEqualTo(updateTags.get(1));

        assertThat(findPostEntity.getPostTags()
                                 .get(2)
                                 .getTag())
                .isNotNull()
                .extracting("name")
                .isEqualTo(updateTags.get(2));

        List<TagEntity> tagEntityList = tagRepository.findAll();
        assertThat(tagEntityList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
    }
}