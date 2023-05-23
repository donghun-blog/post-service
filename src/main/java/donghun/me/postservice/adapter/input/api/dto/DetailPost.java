package donghun.me.postservice.adapter.input.api.dto;

import donghun.me.postservice.application.dto.PostDetailDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DetailPost {
    @Getter
    public static class Response {
        private final Long id;
        private final String title;
        private final String contents;
        private final String thumbnail;
        private final String summary;
        private final List<TagResponse> tags;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;

        private Response(PostDetailDto detailDto) {
            this.id = detailDto.id();
            this.title = detailDto.title();
            this.contents = detailDto.contents();
            this.thumbnail = detailDto.thumbnail();
            this.summary = detailDto.summary();

            this.tags = detailDto.tags()
                     .stream()
                     .map(t -> TagResponse.builder()
                                          .id(t.id())
                                          .name(t.name())
                                          .build())
                     .collect(Collectors.toList());

            this.createdAt = detailDto.createdAt();
            this.modifiedAt = detailDto.modifiedAt();
        }

        public static BaseResponse<DetailPost.Response> success(PostDetailDto detailDto) {
            return BaseResponse.success(new DetailPost.Response(detailDto));
        }
    }
}
