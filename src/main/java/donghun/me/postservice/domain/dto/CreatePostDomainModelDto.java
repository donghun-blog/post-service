package donghun.me.postservice.domain.dto;

import donghun.me.postservice.domain.model.Tag;

import java.util.List;

public record CreatePostDomainModelDto(
        String title,
        String contents,
        String thumbnail,
        boolean visible,
        String summary,
        List<Tag> tags
) {
}
