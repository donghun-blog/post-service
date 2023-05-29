package donghun.me.postservice.domain.model;

import donghun.me.postservice.common.EmptyParameters;
import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import donghun.me.postservice.domain.dto.UpdatePostDomainModelDto;
import donghun.me.postservice.domain.exception.PostException;
import donghun.me.postservice.fixture.PostFixture;
import donghun.me.postservice.fixture.UpdatePostDomainModelDtoFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static donghun.me.postservice.domain.exception.PostErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostTest extends AbstractDefaultTest {

    @DisplayName("게시글 생성 시 제목이 없는 경우 예외를 반환한다.")
    @EmptyParameters
    void createEmptyTitleException(String emptyTitle) {
        // given
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                emptyTitle,
                "contents",
                "thumbnail",
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );
        // when & then
        assertThatThrownBy(() -> Post.Factory.create(createPostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_TITLE_EMPTY);
    }

    @DisplayName("게시글 생성 시 본문이 없는 경우 예외를 반환한다.")
    @EmptyParameters
    void createEmptyContentsException(String emptyContents) {
        // given
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                "title",
                emptyContents,
                "thumbnail",
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );
        // when & then
        assertThatThrownBy(() -> Post.Factory.create(createPostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_CONTENTS_EMPTY);
    }

    @DisplayName("게시글 생성 시 썸네일이 없는 경우 본문에서 썸네일을 추출한다.")
    @EmptyParameters
    void createEmptyThumbnailAndContentsGetThumbnail(String emptyThumbnail) {
        // given
        final String firstImagePath = "bec85c6d-3185-462c-b81d-ace7d795b7dd.png";
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                "title",
                "### 헤더\n" +
                        "![alt text](https://test.com/post/" + firstImagePath + ")'\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.jpeg)\n" +
                        "하단입니다.!",
                emptyThumbnail,
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );

        // when
        Post post = Post.Factory.create(createPostDomainModelDto);

        // then
        assertThat(post)
                .isNotNull()
                .extracting("thumbnail")
                .isEqualTo(firstImagePath);
    }

    @DisplayName("게시글 생성 시 썸네일이 없는 경우와 본문에 이미지가 없는 경우 null을 반환한다.")
    @EmptyParameters
    void createEmptyThumbnailAndContentsThumbnailReturnNull(String emptyThumbnail) {
        // given
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                "title",
                "### 헤더\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "하단입니다.!",
                emptyThumbnail,
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );

        // when
        Post post = Post.Factory.create(createPostDomainModelDto);

        // then
        assertThat(post)
                .isNotNull()
                .extracting("thumbnail")
                .isNull();
    }

    @DisplayName("게시글 생성 시 썸네일이 있는 경우 랜덤ID값을 반환한다.")
    @Test
    void createThumbnailGetRandomId() {
        // given
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                "title",
                "### 헤더\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "하단입니다.!",
                "emptyThumbnail.png",
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );

        // when
        Post post = Post.Factory.create(createPostDomainModelDto);

        // then
        assertThat(post)
                .isNotNull()
                .extracting("thumbnail")
                .isNotNull();

    }

    @DisplayName("게시글 생성 시 썸네일이 없는 경우 본문에서 썸네일을 추출하는데 지원하지 않는 확장자인 경우 예외를 반환한다.")
    @EmptyParameters
    void createEmptyThumbnailGetContentsImageNotSupportExtensionException(String emptyThumbnail) {
        // given
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                "title",
                "### 헤더\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.svg)\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.tiff)\n" +
                        "하단입니다.!",
                emptyThumbnail,
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );

        // when & then
        assertThatThrownBy(() -> Post.Factory.create(createPostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", IMAGE_EXTENSION_NOT_SUPPORT);
    }

    @DisplayName("게시글 생성 시 썸네일이 있고 지원하지 않는 확장자인 경우 예외를 반환한다.")
    @Test
    void createThumbnailNotSupportExtensionException() {
        // given
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                "title",
                "### 헤더\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.svg)\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.tiff)\n" +
                        "하단입니다.!",
                "test.svg",
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );

        // when & then
        assertThatThrownBy(() -> Post.Factory.create(createPostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", IMAGE_EXTENSION_NOT_SUPPORT);
    }

    @DisplayName("게시글 생성 시 썸네일이 없고 본문에도 이미지 없는 경우 null을 반환한다.")
    @EmptyParameters
    void createThumbnailContentsImageReturnNull(String emptyThumbnail) {
        // given
        CreatePostDomainModelDto createPostDomainModelDto = new CreatePostDomainModelDto(
                "title",
                "### 헤더\n" +
                        "본문입니다1.\n" +
                        "### 본문입니다2\n" +
                        "하단입니다.!",
                emptyThumbnail,
                true,
                "summary",
                List.of(
                        Tag.Factory.create(1L, "태그1")
                )
        );

        // when
        Post post = Post.Factory.create(createPostDomainModelDto);

        // then
        assertThat(post)
                .isNotNull()
                .extracting("thumbnail")
                .isNull();
    }

    @DisplayName("게시글 변경 시 제목이 없는 경우 예외를 반환한다.")
    @EmptyParameters
    void updateEmptyTitle(String emptyTitle) {
        // given
        Post post = PostFixture.complete()
                               .build();

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .title(emptyTitle)
                                                                                           .build();
        // when & then
        assertThatThrownBy(() -> post.update(updatePostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_TITLE_EMPTY);
    }

    @DisplayName("게시글 변경 시 본문이 없는 경우 예외를 반환한다.")
    @EmptyParameters
    void updateEmptyContents(String emptyContents) {
        // given
        Post post = PostFixture.complete()
                               .build();

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .contents(emptyContents)
                                                                                           .build();
        // when & then
        assertThatThrownBy(() -> post.update(updatePostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", POST_CONTENTS_EMPTY);
    }

    @DisplayName("게시글 변경 시 썸네일이 있는 경우 지원하지 않는 이미지 확장자인 경우 예외를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "test.svg",
            "test.tiff"
    })
    void updateThumbnailNotSupportFormat(String notSupportThumbnail) {
        // given
        Post post = PostFixture.complete()
                               .build();

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .thumbnail(notSupportThumbnail)
                                                                                           .build();
        // when & then
        assertThatThrownBy(() -> post.update(updatePostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", IMAGE_EXTENSION_NOT_SUPPORT);
    }

    @DisplayName("게시글 변경 시 썸네일이 없는 경우 본문에서 첫번째 이미지가 지원하지 않는 이미지 확장자인 경우 예외를 반환한다.")
    @Test
    void updateContentsFirstImageNotSupportFormat() {
        // given
        Post post = PostFixture.complete()
                               .build();

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .contents("### 헤더\n" +
                                                                                                   "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.svg)\n" +
                                                                                                   "본문입니다1.\n" +
                                                                                                   "### 본문입니다2\n" +
                                                                                                   "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.tiff)\n" +
                                                                                                   "하단입니다.!")
                                                                                           .thumbnail(null)
                                                                                           .build();
        // when & then
        assertThatThrownBy(() -> post.update(updatePostDomainModelDto))
                .isInstanceOf(PostException.class)
                .hasFieldOrPropertyWithValue("errorCode", IMAGE_EXTENSION_NOT_SUPPORT);
    }

    @DisplayName("게시글 변경 시 썸네일이 없는 경우 본문에서 첫번째 이미지가 지원하지 않는 이미지 확장자인 경우 예외를 반환한다.")
    @Test
    void updateThumbnailNullAndContentsFirstImageNotExist() {
        // given
        Post post = PostFixture.complete()
                               .build();

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .contents("### 헤더\n" +
                                                                                                   "본문입니다1.\n" +
                                                                                                   "### 본문입니다2\n" +
                                                                                                   "하단입니다.!")
                                                                                           .thumbnail(null)
                                                                                           .build();
        // when
        post.update(updatePostDomainModelDto);

        // then
        assertTrue(post.isThumbnailEmpty());
    }

    @DisplayName("게시글 업데이트 시 태그가 변경 되었는지 확인한다.")
    @Test
    void updateTagsCheck() {
        // given
        Post post = PostFixture.complete()
                               .tags(
                                       List.of(
                                               Tag.builder()
                                                  .id(1L)
                                                  .name("Spring Boot")
                                                  .build(),
                                               Tag.builder()
                                                  .id(2L)
                                                  .name("Spring Security")
                                                  .build(),
                                               Tag.builder()
                                                  .id(3L)
                                                  .name("OAuth")
                                                  .build(),
                                               Tag.builder()
                                                  .id(4L)
                                                  .name("MSA")
                                                  .build(),
                                               Tag.builder()
                                                  .id(5L)
                                                  .name("Spring Framework")
                                                  .build()
                                       )
                               )
                               .build();


        List<Tag> updateTags = List.of(
                Tag.builder()
                   .id(1L)
                   .name("Update Spring Boot")
                   .build(),
                Tag.builder()
                   .id(2L)
                   .name("Update MSA")
                   .build(),
                Tag.builder()
                   .id(3L)
                   .name("Update Spring Security")
                   .build()
        );

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .tags(updateTags)
                                                                                           .build();

        // when
        post.update(updatePostDomainModelDto);


        // then
        assertThat(post.getTags())
                .isNotNull()
                .isNotEmpty()
                .hasSize(updateTags.size())
                .extracting("id", "name")
                .contains(
                        tuple(updateTags.get(0)
                                        .getId(), updateTags.get(0)
                                                            .getName()),
                        tuple(updateTags.get(1)
                                        .getId(), updateTags.get(1)
                                                            .getName()),
                        tuple(updateTags.get(2)
                                        .getId(), updateTags.get(2)
                                                            .getName())
                )
        ;
    }

    @DisplayName("게시글 도메인 업데이트 후 값을 확인한다.")
    @Test
    void update() {
        // given
        Post post = PostFixture.complete()
                               .build();

        final String updateTitle = "updateTitle";
        final String updateContents = "\"### 업데이트 헤더\\n\" +\n" +
                "                        \"![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.png)\\n\" +\n" +
                "                        \"업데이트 본문입니다1.\\n\" +\n" +
                "                        \"### 업데이트 본문입니다2\\n\" +\n" +
                "                        \"![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.gif)\\n\" +\n" +
                "                        \"업데이트 하단입니다.!\"";
        final boolean updateVisible = false;
        final String updateSummary = "업데이트 요약입니다.";
        final String updateThumbnail = "updateThumbnail.png";

        final List<Tag> updateTags = List.of(
                Tag.builder()
                   .id(1L)
                   .name("updateTag1")
                   .build(),
                Tag.builder()
                   .id(2L)
                   .name("updateTag2")
                   .build(),
                Tag.builder()
                   .id(3L)
                   .name("updateTag3")
                   .build()
        );

        UpdatePostDomainModelDto updatePostDomainModelDto = UpdatePostDomainModelDtoFixture.complete()
                                                                                           .title(updateTitle)
                                                                                           .contents(updateContents)
                                                                                           .visible(updateVisible)
                                                                                           .summary(updateSummary)
                                                                                           .thumbnail(updateThumbnail)
                                                                                           .tags(updateTags)
                                                                                           .build();

        // when
        post.update(updatePostDomainModelDto);

        // then
        assertThat(post)
                .isNotNull()
                .extracting("title", "contents", "visible", "summary")
                .contains(
                        updateTitle,
                        updateContents,
                        updateVisible,
                        updateSummary
                );

        assertFalse(post.isThumbnailEmpty());
        assertThat(post.getThumbnail())
                .isNotBlank();
        assertThat(post.getTags())
                .isNotNull()
                .isNotEmpty()
                .hasSize(updateTags.size())
                .extracting("id", "name")
                .contains(
                        tuple(updateTags.get(0)
                                        .getId(), updateTags.get(0)
                                                            .getName()),
                        tuple(updateTags.get(1)
                                        .getId(), updateTags.get(1)
                                                            .getName()),
                        tuple(updateTags.get(2)
                                        .getId(), updateTags.get(2)
                                                            .getName())
                );

    }
}