package donghun.me.postservice.domain.dto;

import donghun.me.postservice.domain.model.Tag;
import lombok.Builder;

import java.util.List;

public record UpdatePostDomainModelDto(
        String title,
        String contents,
        String thumbnail,
        boolean visible,
        String summary,
        List<Tag> tags
) {

    @Builder
    public UpdatePostDomainModelDto {
    }
}
