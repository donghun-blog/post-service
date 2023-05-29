package donghun.me.postservice.adapter.input.api.dto;

import donghun.me.postservice.application.dto.UpdatePostCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UpdatePost {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "제목은 필수값 입니다.")
        private String title;
        @NotBlank(message = "본문은 필수값 입니다.")
        private String contents;
        @NotNull(message = "공개 여부는 필수값 입니다.")
        private Boolean visible;
        private String summary;
        private List<String> tags;

        @Builder
        public Request(String title, String contents, @NotNull(message = "공개 여부는 필수값 입니다.") Boolean visible, String summary, List<String> tags) {
            this.title = title;
            this.contents = contents;
            this.visible = visible;
            this.summary = summary;
            this.tags = tags;
        }

        public UpdatePostCommand toCommand(MultipartFile thumbnail) {
            return UpdatePostCommand.builder()
                                    .title(title)
                                    .contents(contents)
                                    .visible(visible)
                                    .summary(summary)
                                    .thumbnail(thumbnail)
                                    .tags(Objects.isNull(tags) || tags.isEmpty() ? Collections.emptyList() : tags)
                                    .build();
        }
    }

    @Getter
    public static class Response {
        private final Long postId;

        private Response(Long postId) {
            this.postId = postId;
        }

        public static BaseResponse<UpdatePost.Response> success(Long postId) {
            return BaseResponse.success(new UpdatePost.Response(postId));
        }
    }

}
