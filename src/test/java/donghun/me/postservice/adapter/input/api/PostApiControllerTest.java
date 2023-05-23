package donghun.me.postservice.adapter.input.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import donghun.me.postservice.adapter.input.api.dto.CreatePost;
import donghun.me.postservice.application.port.input.PostCommandUseCase;
import donghun.me.postservice.common.EmptyParameters;
import donghun.me.postservice.common.environment.AbstractPresentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostApiController.class)
class PostApiControllerTest extends AbstractPresentationTest {

    @MockBean
    private PostCommandUseCase postCommandUseCase;

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

    static class MockMultipartFileFixture {
        public static MockMultipartFile complete() {
            return new MockMultipartFile(
                    "thumbnail",
                    "test.png",
                    MULTIPART_FORM_DATA_VALUE,
                    "test.png".getBytes());
        }
    }
}