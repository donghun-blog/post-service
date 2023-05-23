package donghun.me.postservice.application.dto;

import lombok.Builder;

public record TagDto(
        Long id,
        String name
) {
    @Builder
    public TagDto {
    }
}
