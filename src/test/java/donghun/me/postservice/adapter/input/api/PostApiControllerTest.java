package donghun.me.postservice.adapter.input.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import donghun.me.postservice.adapter.input.api.dto.CreatePost;
import donghun.me.postservice.application.dto.PostDetailDto;
import donghun.me.postservice.application.dto.TagDto;
import donghun.me.postservice.application.port.input.PostCommandUseCase;
import donghun.me.postservice.application.port.input.PostQueryUseCase;
import donghun.me.postservice.common.EmptyParameters;
import donghun.me.postservice.common.environment.AbstractPresentationTest;
import donghun.me.postservice.fixture.MockMultipartFileFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostApiController.class)
class PostApiControllerTest extends AbstractPresentationTest {

    @MockBean
    private PostCommandUseCase postCommandUseCase;
    @MockBean
    private PostQueryUseCase postQueryUseCase;

    private static final String BASE_URL = "/api/v1/posts";

    @DisplayName("게시글 등록 시 제목이 없는 경우 예외를 응답한다.")
    @EmptyParameters
    void registerEmptyTitleErrorResponse(String emptyTitle) throws Exception {
        // given
        CreatePost.Request request = CreatePostFixture.complete()
                                                      .title(emptyTitle)
                                                      .build();
        MockMultipartFile mockMultipartFile = MockMultipartFileFixture.complete();
        MockMultipartFile mockAbout = createMockPost(request);

        // when & then
        mockMvc.perform(
                       multipart(BASE_URL)
                               .file(mockMultipartFile)
                               .file(mockAbout))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.body.title").value("제목은 필수값 입니다."))
        ;
    }

    @DisplayName("게시글 등록 시 제목이 없는 경우 예외를 응답한다.")
    @EmptyParameters
    void registerEmptyContentsErrorResponse(String emptyContents) throws Exception {
        // given
        CreatePost.Request request = CreatePostFixture.complete()
                                                      .contents(emptyContents)
                                                      .build();
        MockMultipartFile mockMultipartFile = MockMultipartFileFixture.complete();
        MockMultipartFile mockAbout = createMockPost(request);

        // when & then
        mockMvc.perform(
                       multipart(BASE_URL)
                               .file(mockMultipartFile)
                               .file(mockAbout))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.body.contents").value("본문은 필수값 입니다."))
        ;
    }

    @DisplayName("게시글 등록 시 공개 여부가 null인 경우 예외를 응답한다.")
    @NullSource
    @ParameterizedTest
    void registerVisibleNullErrorResponse(Boolean visible) throws Exception {
        // given
        CreatePost.Request request = CreatePostFixture.complete()
                                                      .visible(visible)
                                                      .build();
        MockMultipartFile mockMultipartFile = MockMultipartFileFixture.complete();
        MockMultipartFile mockAbout = createMockPost(request);

        // when & then
        mockMvc.perform(
                       multipart(BASE_URL)
                               .file(mockMultipartFile)
                               .file(mockAbout))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.body.visible").value("공개 여부는 필수값 입니다."))
        ;
    }

    @DisplayName("게시글 등록 후 게시글 id값을 반환한다.")
    @Test
    void register() throws Exception {
        // given
        CreatePost.Request request = CreatePostFixture.complete()
                .contents("## 1어람어람ㄴㅇㄹㅁㄴㅇ러ㅏㅁㄴ얼\n" +
                        "\n" +
                        "---\n" +
                        "\n" +
                        "### 이러마읾;이리민\n" +
                        " \n" +
                        "![alt text](https://winter-blog-bucket.s3.ap-northeast-2.amazonaws.com/post/b86cc745-4ba6-4b17-a81b-8181c0e19ef6.gif)'\n" +
                        "\n" +
                        "ㄱ,어럼 아래도 이미지\n" +
                        "\n" +
                        "### 기기기긱ㄱㅁㅇㄹ\n" +
                        "\n" +
                        "![alt text](https://winter-blog-bucket.s3.ap-northeast-2.amazonaws.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.jpeg)\n" +
                        "\n" +
                        "ㅁㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇ")
                                                      .build();
        MockMultipartFile mockMultipartFile = MockMultipartFileFixture.complete();
        MockMultipartFile mockAbout = createMockPost(request);

        final Long postId = 1L;
        given(postCommandUseCase.createPost(any()))
                .willReturn(postId);

        // when & then
        mockMvc.perform(
                       multipart(BASE_URL)
                               .file(mockMultipartFile)
                               .file(mockAbout))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.body.postId").value(postId))
        ;
    }

    @DisplayName("게시글 삭제 후 삭제된 게시글 ID를 응답한다.")
    @Test
    void delete() throws Exception {
        // given
        final Long postId = 1L;

        willDoNothing().given(postCommandUseCase)
                       .deletePost(any());

        // when & then
        mockMvc.perform(
                       MockMvcRequestBuilders.delete(BASE_URL + "/{postId}", postId)
               )
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.body.postId").value(postId));
    }

    @DisplayName("게시글 단건 조회 후 상세 정보를 응답한다.")
    @Test
    void detail() throws Exception {
        // given
        final Long postId = 1L;

        final String title=  "제목입니다.";
        final String contents = "본문입니다.";
        final boolean visible = true;
        final String thumbnail = "thumbnail.png";
        final String summary = "요약입니다.";
        List<TagDto> tags = List.of(
                TagDto.builder()
                      .id(1L)
                      .name("Spring Boot")
                      .build(),
                TagDto.builder()
                      .id(2L)
                      .name("Spring Security")
                      .build(),
                TagDto.builder()
                      .id(3L)
                      .name("Spring Cloud")
                      .build()
        );

        final PostDetailDto detailDto = PostDetailDto.builder()
                                                     .id(postId)
                                                     .title(title)
                                                     .contents(contents)
                                                     .visible(visible)
                                                     .thumbnail(thumbnail)
                                                     .summary(summary)
                                                     .tags(tags)
                                                     .createdAt(LocalDate.of(2023, 5, 23)
                                                                         .atStartOfDay())
                                                     .modifiedAt(LocalDate.of(2023, 5, 23)
                                                                          .atStartOfDay())
                                                     .build();

        given(postQueryUseCase.findById(anyLong()))
                .willReturn(detailDto);

        // when & then
        mockMvc.perform(
                       get(BASE_URL + "/{postId}", postId)
               )
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.body.id").value(postId))
               .andExpect(jsonPath("$.body.title").value(title))
               .andExpect(jsonPath("$.body.contents").value(contents))
               .andExpect(jsonPath("$.body.thumbnail").value(thumbnail))
               .andExpect(jsonPath("$.body.summary").value(summary))
               .andExpect(jsonPath("$.body.tags[0].id").value(tags.get(0).id()))
               .andExpect(jsonPath("$.body.tags[0].name").value(tags.get(0).name()))
               .andExpect(jsonPath("$.body.tags[1].id").value(tags.get(1).id()))
               .andExpect(jsonPath("$.body.tags[1].name").value(tags.get(1).name()))
               .andExpect(jsonPath("$.body.tags[2].id").value(tags.get(2).id()))
               .andExpect(jsonPath("$.body.tags[2].name").value(tags.get(2).name()))
               ;
    }

    private MockMultipartFile createMockPost(CreatePost.Request request) throws JsonProcessingException {
        return new MockMultipartFile("post", "json", APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request)
                            .getBytes(StandardCharsets.UTF_8));
    }

    static class CreatePostFixture {
        public static CreatePost.Request.RequestBuilder complete() {
            return CreatePost.Request.builder()
                                     .title("제목입니다.")
                                     .contents("### 헤더\n" +
                                             "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.svg)\n" +
                                             "본문입니다1.\n" +
                                             "### 본문입니다2\n" +
                                             "![alt text](https://test.com/post/ad051fad-f6f0-4f0e-86e2-fec065e76e40.tiff)\n" +
                                             "하단입니다.!")
                                     .visible(true)
                                     .summary("요약입니다.")
                                     .tags(List.of(
                                             "태그1", "태그2"
                                     ))
                    ;
        }
    }
}