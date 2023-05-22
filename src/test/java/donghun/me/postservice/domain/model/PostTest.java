package donghun.me.postservice.domain.model;

import donghun.me.postservice.common.EmptyParameters;
import donghun.me.postservice.common.environment.base.AbstractDefaultTest;
import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import donghun.me.postservice.domain.exception.PostException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static donghun.me.postservice.domain.exception.PostErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
}