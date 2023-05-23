package donghun.me.postservice.application.dto;

import donghun.me.postservice.domain.model.Post;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostDetailDto(
        Long id,
        String title,
        String contents,
        boolean visible,
        String thumbnail,
        String summary,
        List<TagDto> tags,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    @Builder
    public PostDetailDto {
    }

    public static PostDetailDto of(Post post, String absolutePath) {
        return PostDetailDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .contents(post.getContents())
                            .visible(post.isVisible())
                            .thumbnail(
                                    post.isThumbnailEmpty() ? null : absolutePath + post.getThumbnail()
                            )
                            .summary(post.getSummary())
                            .tags(
                                    post.getTags()
                                        .stream()
                                        .map(t -> TagDto.builder()
                                                        .id(t.getId())
                                                        .name(t.getName())
                                                        .build())
                                        .collect(Collectors.toList())
                            )
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build();
    }
}
