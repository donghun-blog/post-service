package donghun.me.postservice.adapter.input.api.dto;

import donghun.me.postservice.application.dto.PostDetailDto;
import donghun.me.postservice.application.dto.PostDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

public class ListPost {
    @Getter
    public static class Response {
        private final Long id;
        private final String title;
        private final String thumbnail;
        private final String summary;
        private final List<TagResponse> tags;
        private final LocalDateTime createdAt;

        private Response(PostDto detailDto) {
            this.id = detailDto.id();
            this.title = detailDto.title();
            this.thumbnail = detailDto.thumbnail();
            this.summary = hasText(detailDto.summary()) ?
                    detailDto.summary() :
                    getPlainContents(detailDto.contents());

            this.tags = detailDto.tags()
                                 .stream()
                                 .map(t -> TagResponse.builder()
                                                      .id(t.id())
                                                      .name(t.name())
                                                      .build())
                                 .collect(Collectors.toList());

            this.createdAt = detailDto.createdAt();
        }

        private String getPlainContents(String markdown) {
            String patternImageTags = "!\\[[^\\]]*\\]\\([^)]+\\)";
            String noImageTags = markdown.replaceAll(patternImageTags, "");

            String patternSpecialCharacters = "[^\\p{L}\\p{N}\\p{Z}]+";
            return noImageTags.replaceAll(patternSpecialCharacters, "").trim();
        }

        public static BaseResponse<Page<ListPost.Response>> success(Page<PostDto> detailDto) {
            return BaseResponse.success(detailDto.map(Response::new));
        }
    }
}
