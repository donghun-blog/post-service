package donghun.me.postservice.application.dto;

import donghun.me.postservice.domain.dto.CreatePostDomainModelDto;
import donghun.me.postservice.domain.dto.UpdatePostDomainModelDto;
import donghun.me.postservice.domain.model.Tag;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.isNull;

public record UpdatePostCommand(
        String title,
        String contents,
        MultipartFile thumbnail,
        boolean visible,
        String summary,
        List<String> tags
) {
    @Builder
    public UpdatePostCommand {
    }

    public UpdatePostDomainModelDto toDomainModelDto(List<Tag> tags) {
        return new UpdatePostDomainModelDto(
                title(),
                contents(),
                isNull(thumbnail) ? null : thumbnail().getOriginalFilename(),
                visible(),
                summary(),
                tags
        );
    }
}
